package com.examen.firebaseproyect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterCompra
import com.examen.firebaseproyect.Clases.Compra
import com.examen.firebaseproyect.Clases.Pedido
import com.examen.firebaseproyect.Clases.Ubicacion
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListCompra : Fragment() {
    private lateinit var rc_compra: RecyclerView
    private lateinit var txt_total_compra: TextView
    private lateinit var btn_enviar_compra: Button
    private var adapterCompra = AdapterCompra()
    lateinit var msDatabaseReference: DatabaseReference
    var listCompras:ArrayList<Compra> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_list_compra, container, false)
        init(view)
        return view
    }
    fun init(view:View)
    {
        msDatabaseReference=FirebaseDatabase.getInstance().getReference()
        rc_compra=view.findViewById(R.id.rc_compra)
        txt_total_compra=view.findViewById(R.id.txt_total_compra)
        btn_enviar_compra=view.findViewById(R.id.btn_enviar_compra)
        btn_enviar_compra.setOnClickListener {

            var db=Conexion().bd(requireContext())
            var cursor=db.rawQuery("select * from Usuario", null)
            cursor.moveToFirst()
            var idUsuario=cursor.getString(0)
            db.close()
            msDatabaseReference!!.child("Ubicacion").orderByChild("idUsuario").equalTo(idUsuario).addListenerForSingleValueEvent(
                object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot){
                        if (snapshot.exists()) {

                            var ubicacion = Ubicacion()
                            for (postSnapshot in snapshot.children) {
                                ubicacion = postSnapshot.getValue(Ubicacion::class.java)!!
                            }
                            val keyUbicacion=ubicacion.key

                            var db=Conexion().bd(requireContext())
                            var cursor=db.rawQuery("select * from Usuario", null)
                            cursor.moveToFirst()
                            var cursor_compra=db.rawQuery("select sum(subtotal) from Compra",null)
                            cursor_compra.moveToFirst()

                            var id=msDatabaseReference!!.push().key
                            var keyUsuario=cursor.getString(0)
                            var total=cursor_compra.getString(0)
                            var pedido= Pedido(id.toString(),keyUsuario,keyUbicacion,total,
                                SimpleDateFormat("dd/M/y hh:mm:ss").format(Date()),"0")
                            msDatabaseReference.child("Pedido").child(id.toString()).setValue(pedido)

                            var cursor_pedido_detalle=db.rawQuery("select * from Compra",null)
                            while (cursor_pedido_detalle.moveToNext())
                            {
                                var id_pedido_detalle=msDatabaseReference!!.push().key
                                var compra= Compra(id_pedido_detalle.toString(),cursor_pedido_detalle.getString(1),
                                    id.toString(),cursor_pedido_detalle.getString(2),cursor_pedido_detalle.getString(3),cursor_pedido_detalle.getString(4),cursor_pedido_detalle.getString(5))
                                msDatabaseReference.child("PedidoDetalle").child(id_pedido_detalle.toString()).setValue(compra)
                            }

                            db.execSQL("delete from Compra")
                            listCompras.clear()
                            adapterCompra.notifyDataSetChanged()
                            txt_total_compra.text="Total = $0.00"
                            db.close()


                        } else
                        {
                            Toast.makeText(requireContext(),"No tiene una Ubicacion",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )


        }
        rc_compra.setHasFixedSize(true)
        rc_compra.layoutManager=LinearLayoutManager(requireContext())
        get_compras()
        delete_swipe()

    }

    fun get_compras()
    {
        var db=Conexion().bd(requireContext())
        var cursor=db.rawQuery("select * from Compra",null)
        var cursor_total=db.rawQuery("select sum(subtotal) from Compra", null)
        cursor_total.moveToFirst()
        if (cursor_total.getString(0)!=null)
        {

            txt_total_compra.text="TOTAL:"+cursor_total.getString(0).toFloat().toString()+"$"
        }




        while(cursor.moveToNext())
        {
            var compra=Compra()
            compra.idProducto=cursor.getString(1)
            compra.cantidad=cursor.getString(2)
            compra.precio=cursor.getString(3)
            compra.subtotal=cursor.getString(4)
            compra.sugerencia=cursor.getString(5)
            listCompras.add(compra)
        }
        db.close()
        adapterCompra.data(listCompras,requireContext(),msDatabaseReference,txt_total_compra)
        rc_compra.adapter=adapterCompra
        adapterCompra.notifyDataSetChanged()

    }

    fun delete_swipe()
    {
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

                var db=Conexion().bd(requireContext())
                db.delete(Utilidades.TABLA_COMPRA, "id=" + ((viewHolder.adapterPosition)+1).toString(), null)


                var cursor_total=db.rawQuery("select sum(subtotal) from Compra", null)
                cursor_total.moveToFirst()

                if(cursor_total.getString(0)==null)
                {
                    txt_total_compra.text="TOTAL: 0.00"
                }
                else
                {
                    txt_total_compra.text="TOTAL:"+cursor_total.getString(0).toFloat().toString()+"$"
                }

                db.close()
                listCompras.removeAt(viewHolder.adapterPosition)
                adapterCompra.notifyDataSetChanged()

            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rc_compra)
    }


}