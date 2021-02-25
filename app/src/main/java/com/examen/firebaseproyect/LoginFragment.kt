package com.examen.firebaseproyect

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.examen.firebaseproyect.Clases.Usuario
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.ConexionSQLiteHelper
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_APELLIDOS
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_CELULAR
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_EDAD
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_IDUSUARIO
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_NOMBRE
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_PASSWORD
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_TIPO
import com.example.koalito.Bd.utilidades.Utilidades.CAMPO_USERNAME
import com.example.koalito.Bd.utilidades.Utilidades.TABLA_USUARIOS
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    //text
    private lateinit var txt_usuario:TextInputEditText
    private lateinit var txt_password:TextInputEditText
    //
    private lateinit var btn_registrar:Button
    private lateinit var btn_aceptar:Button
    //firebase database
    private var mDatabaseReference: DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      var view= inflater.inflate(R.layout.fragment_login, container, false)
     init(view)
        return view
    }
    fun init(view: View)
    {
        //
        txt_usuario=view.findViewById(R.id.txt_usuario)
        txt_password=view.findViewById(R.id.txt_password)

        // firebase
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Usuario");
        btn_registrar=view.findViewById(R.id.btn_registrar)
        btn_registrar.setOnClickListener {
            val fragmentManager=getFragmentManager()
            val fragmentTransaction= fragmentManager?.beginTransaction()
            val fragment= PrimerRegistroFragment()
            fragmentTransaction?.replace(R.id.fragment_container_firstactivity,fragment)
                ?.addToBackStack(null)
            fragmentTransaction?.commit()

        }
        btn_aceptar=view.findViewById(R.id.btn_acpetar)

        btn_aceptar.setOnClickListener {
            mDatabaseReference!!.orderByChild("usuario").equalTo(txt_usuario.text.toString()).addListenerForSingleValueEvent(
                object : ValueEventListener
                {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists())
                        {
                          mDatabaseReference!!.orderByChild("password").equalTo(txt_password.text.toString()).addListenerForSingleValueEvent(
                              object :ValueEventListener{
                                  override fun onDataChange(snapshot: DataSnapshot) {
                                     if (snapshot.exists())
                                     {
                                         var usuario:Usuario= Usuario()
                                         for (postSnapshot in snapshot.children) {
                                             usuario = postSnapshot.getValue(Usuario::class.java)!!
                                         }
                                         var db= Conexion().bd(requireContext())
                                         var values=ContentValues()
                                         values.put(CAMPO_IDUSUARIO,usuario.key)
                                         values.put(CAMPO_USERNAME,usuario.usuario)
                                         values.put(CAMPO_PASSWORD,usuario.password)
                                         values.put(CAMPO_NOMBRE,usuario.nombre)
                                         values.put(CAMPO_APELLIDOS,usuario.apellidos)
                                         values.put(CAMPO_EDAD,usuario.edad)
                                         values.put(CAMPO_CELULAR,usuario.celular)
                                         values.put(CAMPO_TIPO,usuario.tipo)
                                         db.insert(TABLA_USUARIOS,null,values)
                                         db.close()


                                         val intent = Intent(it.context, MainActivity::class.java)
                                         intent.flags =
                                             Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                         startActivity(intent)
                                     }
                                      else
                                     {
                                         Toast.makeText(requireContext(),"No Existe",Toast.LENGTH_SHORT).show()
                                     }
                                  }

                                  override fun onCancelled(error: DatabaseError) {
                                      TODO("Not yet implemented")
                                  }
                              }
                          )
                        }
                        else
                        {
                            Toast.makeText(requireContext(),"No Existe",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

        }
    }


}
