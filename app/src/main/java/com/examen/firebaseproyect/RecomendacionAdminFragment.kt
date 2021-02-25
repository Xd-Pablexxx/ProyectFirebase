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
import com.examen.firebaseproyect.Adapter.AdapterRecomendacion
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.Clases.Recomendacion
import com.examen.firebaseproyect.Clases.UsuarioProducto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class RecomendacionAdminFragment : Fragment() {
    private lateinit var  rc_recomendacion: RecyclerView
    // boton flotante
    private lateinit var btn_floating_agregar: FloatingActionButton
    //listado
    private var list_recomendacion:ArrayList<Recomendacion> = ArrayList()
    //firebase database
    private lateinit var msDatabaseReference: DatabaseReference
    private var mStorage: FirebaseStorage? = null
    //adaptador
    private  var adapterRecomendacion: AdapterRecomendacion = AdapterRecomendacion()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root=inflater.inflate(R.layout.fragment_recomendacion_admin, container, false)
        init(root)
        return root;
    }
    fun init(view:View) {
        btn_floating_agregar = view.findViewById(R.id.btn_flotating_agregar)
        rc_recomendacion = view.findViewById(R.id.rc_recomendacion)
        rc_recomendacion .setHasFixedSize(true)
        msDatabaseReference = FirebaseDatabase.getInstance().getReference()

        rc_recomendacion!!.layoutManager = LinearLayoutManager(requireContext())

        btn_floating_agregar.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.addRecomendacionAdminFragment)
        }
        Content(view)
        deleteSwipe()
    }
    private fun Content(view:View) {
        msDatabaseReference !!.child("Recomendacion").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_recomendacion.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val product = postSnapshot.getValue(Recomendacion::class.java)
                    list_recomendacion!!.add(product!!)

                }
                adapterRecomendacion?.data(list_recomendacion,view.context)
                rc_recomendacion!!.adapter = adapterRecomendacion
                adapterRecomendacion!!.notifyDataSetChanged()
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
                val imageRef = mStorage!!.getReferenceFromUrl(list_recomendacion.get(viewHolder.adapterPosition).img!!)
                imageRef.delete().addOnSuccessListener {

                    Toast.makeText(requireContext(), "Recomendacion Eliminada", Toast.LENGTH_SHORT).show()
                }
                msDatabaseReference.child("Recomendacion").child(list_recomendacion.get(viewHolder.adapterPosition).key!!).removeValue();

                //  msDatabaseReference.child("Producto").child(list_categoria.get(viewHolder.adapterPosition).key!!).removeValue();
                list_recomendacion.removeAt(viewHolder.adapterPosition)
                adapterRecomendacion.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rc_recomendacion)
    }



}