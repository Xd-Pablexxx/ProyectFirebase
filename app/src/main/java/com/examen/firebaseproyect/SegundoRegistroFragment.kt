package com.examen.firebaseproyect

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.examen.firebaseproyect.Clases.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SegundoRegistroFragment : Fragment(), View.OnClickListener {
    private lateinit var txt_nombre: TextInputEditText
    private lateinit var txt_apellidos: TextInputEditText
    private lateinit var txt_edad: TextInputEditText
    private lateinit var txt_celular: TextInputEditText
    //Bundle
    private lateinit var b_username:String
    private lateinit var b_password:String
    //Layout
    private lateinit var TL_nombre:TextInputLayout
    private lateinit var TL_apellidos:TextInputLayout
    private lateinit var TL_edad:TextInputLayout
    private lateinit var TL_celular:TextInputLayout
    //Button
    private lateinit var btn_guardar:Button
    //firebase database
    private var mDatabaseReference: DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_segundo_registro, container, false)
        val bundle=arguments
        b_username= bundle?.getString("Username")!!
        b_password=bundle?.getString("Password")!!
        init(view)
        return view
    }
    fun init(view: View)
    {
        txt_nombre=view.findViewById(R.id.txt_nombre)
        txt_apellidos=view.findViewById(R.id.txt_apellidos)
        txt_edad=view.findViewById(R.id.txt_edad)
        txt_celular=view.findViewById(R.id.txt_celular)

        //
        TL_nombre=view.findViewById(R.id.TL_nombre)
        TL_apellidos=view.findViewById(R.id.TL_apellidos)
        TL_edad=view.findViewById(R.id.TL_edad)
        TL_celular=view.findViewById(R.id.TL_celular)


        //button
        btn_guardar=view.findViewById(R.id.btn_guardar)
        btn_guardar.setOnClickListener(this)
        // firebase
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Usuario");
    }

    override fun onClick(p0: View?) {
       var id=p0?.id
        when(id)
        {
            R.id.btn_guardar->
            {
                if(txt_nombre.text.toString()=="")
                {
                    TL_nombre.error="Datos vacio"
                }
                else if(txt_apellidos.text.toString()=="")
                {
                    TL_apellidos.error="Datos vacio"
                }
                else if(txt_edad.text.toString()=="")
                {
                    TL_edad.error="Datos vacio"
                }
                else if(txt_celular.text.toString()=="")
                {
                    TL_celular.error="Datos vacio"
                }
                else
                {


                    val id = mDatabaseReference!!.push().key

                    val mProduct = Usuario(id.toString(),b_username,b_password,
                        txt_nombre.text.toString(),txt_apellidos.text.toString(),txt_edad.text.toString(),
                        txt_celular.text.toString(),SimpleDateFormat("dd/M/y hh:mm:ss").format(Date()),"cliente")
                    if (id != null) {


                        mDatabaseReference!!.child(id).setValue(mProduct)



                        Toast.makeText(requireContext(),"Usuario Creado",Toast.LENGTH_SHORT).show()
                        val fragmentManager=getFragmentManager()
                        val fragmentTransaction= fragmentManager?.beginTransaction()
                        val fragment= LoginFragment()
                        fragmentTransaction?.replace(R.id.fragment_container_firstactivity,fragment)
                            ?.addToBackStack(null)
                        fragmentTransaction?.commit()
                    }

                }
            }
        }
    }


}