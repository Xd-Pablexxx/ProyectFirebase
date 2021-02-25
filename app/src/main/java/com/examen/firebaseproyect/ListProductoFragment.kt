package com.examen.firebaseproyect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterProducto
import com.examen.firebaseproyect.Adapter.AdapterProductoCliente
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ListProductoFragment : Fragment() {
    private lateinit var  rc_producto: RecyclerView
    //listado
    private var list_producto:ArrayList<Producto> = ArrayList()
    //firebase database
    private lateinit var msDatabaseReference: DatabaseReference
    //adaptador
    private  var adapterproducto= AdapterProductoCliente()
    var Idcategoria:String?=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Idcategoria=arguments?.getString("keyCategoria")
        var view= inflater.inflate(R.layout.fragment_list_producto, container, false)
        init(view)
        return view
    }
    fun init(view:View) {

        rc_producto = view.findViewById(R.id.rc_producto)
        rc_producto .setHasFixedSize(true)
        msDatabaseReference = FirebaseDatabase.getInstance().getReference()

        rc_producto!!.layoutManager = GridLayoutManager(requireContext(),1)


        Content(view)

    }
    fun id(view: View):String{
        var db= Conexion().bd(view.context)
        var cursor=db.rawQuery("Select * from ${Utilidades.TABLA_USUARIOS}",null)
        cursor.moveToFirst()
        var id=cursor.getString(0)
        db.close()
        return id

    }
    private fun Content(view:View) {
        msDatabaseReference!!.child("Producto").orderByChild("keycategoria").equalTo(Idcategoria).addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        list_producto.clear()
                        for (postSnapshot in snapshot.children) {
                            val product = postSnapshot.getValue(Producto::class.java)
                            list_producto!!.add(product!!)

                        }
                        adapterproducto?.data(list_producto,view.context,view,msDatabaseReference,id(view))
                        rc_producto!!.adapter =adapterproducto
                        adapterproducto!!.notifyDataSetChanged()

                    }
                    else
                    {
                        Toast.makeText(requireContext(),"No Existe", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }


}