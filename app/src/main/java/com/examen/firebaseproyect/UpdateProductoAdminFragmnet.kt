package com.examen.firebaseproyect

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import java.io.ByteArrayOutputStream


class UpdateProductoAdminFragmnet : Fragment(), View.OnClickListener  {
    private lateinit var txt_nombre_producto: TextInputEditText
    private lateinit var txt_precio_producto: TextInputEditText
    private lateinit var txt_descripcion_producto: TextInputEditText
    //Buttons
    private lateinit var btn_agregar_imagen: Button
    private lateinit var btn_floating_agregar: ExtendedFloatingActionButton
    //Img
    private lateinit var img_producto: ImageView
    private lateinit var mImageUri: Uri;
    private var mStorage: FirebaseStorage? = null
    // firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    //progresbar
    private lateinit var pgb_producto: ProgressBar
    //
    private var Idcategoria:String?=""
    private var key=""
    private var img=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root=inflater.inflate(R.layout.fragment_update_producto_admin_fragmnet, container, false)
        init(root)
        var producto=arguments?.getSerializable("Producto") as Producto
        key=producto.key
        img=producto.img
        Idcategoria=producto.keycategoria
        txt_nombre_producto.setText(producto.nombre)
        txt_descripcion_producto.setText(producto.descripcion)
        txt_precio_producto.setText(producto.precio)
        Glide.with(this).asBitmap().load(img).into(object : SimpleTarget<Bitmap?>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                img_producto.setImageBitmap(resource)
                mImageUri=getImageUriFromBitmap(requireContext(),resource)
            }
        })
        return root;
    }
    fun init(root:View)
    {
        pgb_producto=root.findViewById(R.id.pgb_producto)
        //
        img_producto=root.findViewById(R.id.img_producto)
        //
        txt_nombre_producto=root.findViewById(R.id.txt_nombre_producto)
        txt_precio_producto=root.findViewById(R.id.txt_precio_producto)

        txt_descripcion_producto=root.findViewById(R.id.txt_descripcion_producto)


        //buttons
        btn_agregar_imagen=root.findViewById(R.id.btn_agregar_imagen)
        btn_floating_agregar=root.findViewById(R.id.btn_floating_agregar)

        btn_agregar_imagen.setOnClickListener(this)
        btn_floating_agregar.setOnClickListener(this)

        //firebase
        /// la imagenes

        mStorageRef = FirebaseStorage.getInstance().getReference("Producto");
        // Base datos
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Producto");
        //



    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.btn_floating_agregar->
            {
                pgb_producto.visibility=View.VISIBLE
                btn_floating_agregar.visibility=View.GONE
                val fileReference: StorageReference = mStorageRef!!.child(
                    System.currentTimeMillis()
                        .toString() + "." + getFileExtension(mImageUri)
                )

                mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener { taskSnapshot ->

                        val firebaseUri = taskSnapshot.storage.downloadUrl
                        firebaseUri.addOnSuccessListener { uri ->
                            val url = uri.toString()

                            mStorage = FirebaseStorage.getInstance()
                            val imageRef = mStorage!!.getReferenceFromUrl(img)
                            imageRef.delete().addOnSuccessListener {

                                Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_LONG).show()
                            }

                            val precio= txt_precio_producto!!.text.toString()
                            val nombre=txt_nombre_producto!!.text.toString()

                            val descripcion=txt_descripcion_producto!!.text.toString()



                            val mProduct = Producto(
                                key,nombre,precio,descripcion,Idcategoria.toString(),url
                            )
                            if (id != null) {
                                mDatabaseReference!!.child(key).setValue(mProduct)
                            }
                            pgb_producto.visibility=View.GONE
                            btn_floating_agregar.visibility=View.VISIBLE

                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        //val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        ///mProgressBar.setProgress(progress.toInt())
                    }
            }
            R.id.btn_agregar_imagen->
            {
                Log.i("info","llego")
                val intent= Intent()
                intent.type="image/*"
                intent.action= Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Selecciona una imagen"),
                    1
                )
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK
            && data != null && data.getData() != null) {
            mImageUri = data.getData()!!;
            Glide.with(this).load(mImageUri).into(img_producto);

        }
    }
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
    private fun getFileExtension(uri: Uri): String? {
        val cR = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }


}