package com.examen.firebaseproyect

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Recomendacion
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import java.io.ByteArrayOutputStream


class UpdateRecomendacionAdminFragment : Fragment(), View.OnClickListener {
    private lateinit var txt_nombre_recomendacion: TextInputEditText
    private lateinit var txt_precio_recomendacion: TextInputEditText
    //Buttons
    private lateinit var btn_agregar_imagen: Button
    private lateinit var btn_floating_actualizar: ExtendedFloatingActionButton
    //Img
    private lateinit var img_recomendacion: ImageView
    private lateinit var mImageUri: Uri;
    // firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mStorage: FirebaseStorage? = null
    private var mUploadTask: StorageTask<*>? = null
    //progresbar
    private lateinit var pgb_recomendacion: ProgressBar
    private var key=""
    private var img=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root=inflater.inflate(R.layout.fragment_update_recomendacion_admin, container, false)
        init(root)
        var recomendacion=arguments?.getSerializable("Recomendacion") as Recomendacion
        key=recomendacion.key
        img=recomendacion.img
        txt_nombre_recomendacion.setText(recomendacion.nombre)
        txt_precio_recomendacion.setText(recomendacion.precio)
        Glide.with(this).asBitmap().load(img).into(object : SimpleTarget<Bitmap?>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                img_recomendacion.setImageBitmap(resource)
                mImageUri=getImageUriFromBitmap(requireContext(),resource)
            }
        })
        return root
    }
    fun init(root:View)
    {
        pgb_recomendacion=root.findViewById(R.id.pgb_recomendacion)
        //
        img_recomendacion=root.findViewById(R.id.img_recomendacion)
        //
        txt_nombre_recomendacion=root.findViewById(R.id.txt_nombre_recomendacion)
        txt_precio_recomendacion=root.findViewById(R.id.txt_precio_recomendacion)

        //buttons
        btn_agregar_imagen=root.findViewById(R.id.btn_agregar_imagen)
        btn_floating_actualizar=root.findViewById(R.id.btn_floating_actualizar)

        btn_agregar_imagen.setOnClickListener(this)
        btn_floating_actualizar.setOnClickListener(this)

        //firebase
        /// la imagenes

        mStorageRef = FirebaseStorage.getInstance().getReference("Recomendacion");
        // Base datos
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Recomendacion");

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK
            && data != null && data.getData() != null) {
            mImageUri = data.getData()!!;
            Glide.with(this).load(mImageUri).into(img_recomendacion);

        }
    }
    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.btn_floating_actualizar->
            {
                pgb_recomendacion.visibility=View.VISIBLE
                btn_floating_actualizar.visibility=View.GONE
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

                            val precio = txt_precio_recomendacion!!.text.toString()
                            val nombre=txt_nombre_recomendacion!!.text.toString()


                            val categoria = Recomendacion(key,nombre,precio,url)

                            mDatabaseReference!!.child(key).setValue(categoria)

                            pgb_recomendacion.visibility=View.GONE
                            btn_floating_actualizar.visibility=View.VISIBLE
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
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
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