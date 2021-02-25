package com.examen.firebaseproyect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Recomendacion
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask


class AddRecomendacionAdminFragment : Fragment(), View.OnClickListener {

    private lateinit var txt_nombre_recomendacion: TextInputEditText
    private lateinit var txt_precio_recomendacion: TextInputEditText
    //Buttons
    private lateinit var btn_agregar_imagen: Button
    private lateinit var btn_floating_agregar: ExtendedFloatingActionButton
    //Img
    private lateinit var img_recomendacion: ImageView
    private lateinit var mImageUri: Uri;
    // firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    //progresbar
    private lateinit var pgb_recomendacion: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      var root=inflater.inflate(R.layout.fragment_add_recomendacion_admin, container, false)
        init(root)
        return root;
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
        btn_floating_agregar=root.findViewById(R.id.btn_floating_agregar)

        btn_agregar_imagen.setOnClickListener(this)
        btn_floating_agregar.setOnClickListener(this)

        //firebase
        /// la imagenes

        mStorageRef = FirebaseStorage.getInstance().getReference("Recomendacion");
        // Base datos
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Recomendacion");

    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.btn_floating_agregar->
            {
                pgb_recomendacion.visibility=View.VISIBLE
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

                            Toast.makeText(requireContext(), "successful", Toast.LENGTH_LONG).show()

                            val precio= txt_precio_recomendacion!!.text.toString()
                            val nombre=txt_nombre_recomendacion!!.text.toString()

                            val id = mDatabaseReference!!.push().key
                            val mProduct = Recomendacion(
                                id!!,nombre,precio,url
                            )
                            if (id != null) {
                                mDatabaseReference!!.child(id).setValue(mProduct)
                            }
                            pgb_recomendacion.visibility=View.GONE
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
            Glide.with(this).load(mImageUri).into(img_recomendacion);

        }
    }
    private fun getFileExtension(uri: Uri): String? {
        val cR = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }


}