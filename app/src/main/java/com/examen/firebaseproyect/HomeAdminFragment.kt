package com.examen.firebaseproyect

import android.os.Bundle
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
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.Clases.UsuarioProducto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class HomeAdminFragment : Fragment() {
    private lateinit var  rc_categoria:RecyclerView
    // boton flotante
    private lateinit var btn_floating_agregar:FloatingActionButton
    //listado
    private var list_categoria:ArrayList<Categoria> = ArrayList()
    //firebase database
    private lateinit var msDatabaseReference:DatabaseReference
    private var mStorage: FirebaseStorage? = null
    //adaptador
    private  var adapterCategoria: AdapterCategoria= AdapterCategoria()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_home_admin, container, false)
        init(root)
        return root
    }
    fun init(view:View) {
        btn_floating_agregar = view.findViewById(R.id.btn_flotating_agregar)
        rc_categoria = view.findViewById(R.id.rc_categoria)
        rc_categoria .setHasFixedSize(true)
        msDatabaseReference = FirebaseDatabase.getInstance().getReference()

        rc_categoria!!.layoutManager = LinearLayoutManager(requireContext())

        btn_floating_agregar.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.addCategoriaAdminFragment)
        }
        Content(view)
        deleteSwipe()
    }
    private fun Content(view:View) {
        msDatabaseReference !!.child("Categoria").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_categoria.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val product = postSnapshot.getValue(Categoria::class.java)
                    list_categoria!!.add(product!!)

                }
                adapterCategoria?.data(list_categoria,view.context)
                rc_categoria!!.adapter = adapterCategoria
                adapterCategoria!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
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
                mStorage = FirebaseStorage.getInstance()
                val imageRef = mStorage!!.getReferenceFromUrl(list_categoria.get(viewHolder.adapterPosition).img!!)
                imageRef.delete().addOnSuccessListener {

                    Toast.makeText(requireContext(), "Categoria Eliminada", Toast.LENGTH_SHORT).show()
                }
                msDatabaseReference.child("Categoria").child(list_categoria.get(viewHolder.adapterPosition).key!!).removeValue();
                msDatabaseReference!!.child("Producto").orderByChild("keycategoria").equalTo(list_categoria.get(viewHolder.adapterPosition).key!!).addListenerForSingleValueEvent(
                    object : ValueEventListener
                    {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                            {

                                for (postSnapshot in snapshot.children) {
                                    var product = postSnapshot.getValue(Producto::class.java)
                                    val imageRef = mStorage!!.getReferenceFromUrl(product!!.img!!)
                                    imageRef.delete().addOnSuccessListener {
                                    }
                                    msDatabaseReference.child("Producto").child(product!!.key).removeValue();
                                    msDatabaseReference.child("UsuarioProducto").orderByChild("keyProducto").equalTo(product!!.key).addListenerForSingleValueEvent(
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
              //  msDatabaseReference.child("Producto").child(list_categoria.get(viewHolder.adapterPosition).key!!).removeValue();
                list_categoria.removeAt(viewHolder.adapterPosition)
                adapterCategoria.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rc_categoria)
    }



}