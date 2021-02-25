package com.examen.firebaseproyect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterCategoriaVertical
import com.examen.firebaseproyect.Adapter.AdapterProductoClienteHome
import com.examen.firebaseproyect.Adapter.AdapterRecomendacionCliente
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Compra
import com.examen.firebaseproyect.Clases.Recomendacion
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var  rc_categoria: RecyclerView
    private lateinit var rc_recomendacion:RecyclerView
    //listado
    private var list_categoria:ArrayList<Categoria> = ArrayList()
    private var list_recomendacion:ArrayList<Recomendacion> = ArrayList()
    //firebase database
    private lateinit var msDatabaseReference: DatabaseReference
    //adaptador
    private var adapterCategoriaVertical: AdapterCategoriaVertical = AdapterCategoriaVertical()
    private var adapterRecomendacion:AdapterRecomendacionCliente= AdapterRecomendacionCliente()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onStart() {
        super.onStart()
        var db= Conexion().bd(requireContext())
        var cursor=db.rawQuery("Select * from ${Utilidades.TABLA_USUARIOS}",null)
        cursor.moveToFirst()
        if(cursor.getString(7)=="admin")
        {
            Navigation.findNavController(requireView())
                .navigate(R.id.homeAdminFragment);
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }
    fun init(view: View)
    {
        rc_categoria = view.findViewById(R.id.rc_categoria)
        rc_recomendacion=view.findViewById(R.id.rc_recomendacion)
        rc_categoria .setHasFixedSize(true)
        rc_recomendacion.setHasFixedSize(true)
        msDatabaseReference = FirebaseDatabase.getInstance().getReference()

        rc_categoria.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rc_recomendacion.layoutManager=LinearLayoutManager(requireContext())


        Content(view)

    }
    private fun Content(view:View) {
        msDatabaseReference .child("Categoria").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_categoria.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val product = postSnapshot.getValue(Categoria::class.java)
                    list_categoria!!.add(product!!)

                }
                adapterCategoriaVertical?.data(list_categoria,view.context)

                rc_categoria.adapter = adapterCategoriaVertical
                adapterCategoriaVertical!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
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
        /*msDatabaseReference.child("PedidoDetalle").addValueEventListener(object:
        ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list_Producto.clear()
                var db=Conexion().bd(requireContext())
                for (postSnapshot in snapshot.children) {
                    val compra = postSnapshot.getValue(Compra::class.java)
                    db.execSQL("insert into Compra(idproducto) values ('${compra?.idProducto}')")

                }
                var cursor= db.rawQuery("select idproducto from Compra group by idproducto order by count(idproducto) DESC  LIMIT 10",null)
                while(cursor.moveToNext())
                {
                   list_Producto.add(cursor.getString(0))
                }
                db.execSQL("delete from Compra")
                adapterProductoClienteHome.data(list_Producto,requireContext(),msDatabaseReference)
                rc_producto_mas_vendido.adapter=adapterProductoClienteHome
                adapterProductoClienteHome.notifyDataSetChanged()
                db.close()



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })*/
    }



}