package com.examen.firebaseproyect

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.fragment_add_producto_admin.*
import kotlinx.android.synthetic.main.fragment_update_categoria_admin.*

class AddProductoAdminFragment : Fragment(), View.OnClickListener {
    private lateinit var txt_nombre_producto: TextInputEditText
    private lateinit var txt_precio_producto: TextInputEditText
    private lateinit var txt_descripcion_producto:TextInputEditText
    //Buttons
    private lateinit var btn_agregar_imagen: Button
    private lateinit var btn_floating_agregar: ExtendedFloatingActionButton
    //Img
    private lateinit var img_producto: ImageView
    private lateinit var mImageUri: Uri;
    // firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    //progresbar
    private lateinit var pgb_producto: ProgressBar
    //
    private var Idcategoria:String?=""
    //


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Idcategoria=arguments?.getString("keyCategoria")
        var root= inflater.inflate(R.layout.fragment_add_producto_admin, container, false)
        init(root)
        return  root
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
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
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

                            Toast.makeText(requireContext(), " successful", Toast.LENGTH_LONG).show()

                            val precio= txt_precio_producto!!.text.toString()
                            val nombre=txt_nombre_producto!!.text.toString()

                            val descripcion=txt_descripcion_producto!!.text.toString()


                            val id = mDatabaseReference!!.push().key
                            val mProduct = Producto(
                                id!!,nombre,precio,descripcion,Idcategoria.toString(),url
                            )
                            if (id != null) {
                                mDatabaseReference!!.child("Producto").child(id).setValue(mProduct)
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
    private fun getFileExtension(uri: Uri): String? {
        val cR = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

}