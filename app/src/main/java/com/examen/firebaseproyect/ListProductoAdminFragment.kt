package com.examen.firebaseproyect

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterCategoria
import com.examen.firebaseproyect.Adapter.AdapterProducto
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.Clases.Usuario
import com.examen.firebaseproyect.Clases.UsuarioProducto
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ListProductoAdminFragment : Fragment() {
    private lateinit var  rc_producto: RecyclerView
    // boton flotante
    private lateinit var btn_floating_agregar: FloatingActionButton
    //listado
    private var list_producto:ArrayList<Producto> = ArrayList()
    //firebase database
    private lateinit var msDatabaseReference: DatabaseReference
    private var mStorage: FirebaseStorage? = null
    //adaptador
    private  var adapterproducto= AdapterProducto()
    var Idcategoria:String?=""

    //progreso
    private lateinit var pgb_linea_producto:LinearProgressIndicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Idcategoria=arguments?.getString("keyCategoria")
        var root= inflater.inflate(R.layout.fragment_list_producto_admin, container, false)

        init(root)
        return root
    }
    fun init(view:View) {
        pgb_linea_producto=view.findViewById(R.id.pgb_linea_producto)
        btn_floating_agregar = view.findViewById(R.id.btn_flotating_agregar)
        rc_producto = view.findViewById(R.id.rc_producto)
        rc_producto .setHasFixedSize(true)
        msDatabaseReference = FirebaseDatabase.getInstance().getReference()

        rc_producto!!.layoutManager = LinearLayoutManager(requireContext())

        btn_floating_agregar.setOnClickListener {
            var bundle=Bundle()
            bundle.putString("keyCategoria",Idcategoria)
            Navigation.findNavController(requireView()).navigate(R.id.addProductoAdminFragment,bundle)
        }
        Content(view)
        deleteSwipe()
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
                        adapterproducto?.data(list_producto,view.context)
                        rc_producto!!.adapter =adapterproducto
                        adapterproducto!!.notifyDataSetChanged()

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
    private fun deleteSwipe() {
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                pgb_linea_producto.visibility=View.VISIBLE

                mStorage = FirebaseStorage.getInstance()
                val imageRef = mStorage!!.getReferenceFromUrl(list_producto.get(viewHolder.adapterPosition).img!!)
                imageRef.delete().addOnSuccessListener {

                    Toast.makeText(requireContext(), "Producto Eliminada", Toast.LENGTH_SHORT).show()
                    pgb_linea_producto.visibility=View.GONE
                }
                msDatabaseReference.child("Producto").child(list_producto.get(viewHolder.adapterPosition).key!!).removeValue();
                msDatabaseReference.child("UsuarioProducto").orderByChild("keyProducto").equalTo(list_producto.get(viewHolder.adapterPosition).key!!).addListenerForSingleValueEvent(
                    object : ValueEventListener
                    {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                            {
                                for (postSnapshot in snapshot.children) {
                                    val usuarioProducto = postSnapshot.getValue(UsuarioProducto::class.java)
                                    msDatabaseReference.child("UsuarioProducto").child(usuarioProducto!!.key).removeValue();
                                }
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
                list_producto.removeAt(viewHolder.adapterPosition)
                adapterproducto.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rc_producto)
    }
}