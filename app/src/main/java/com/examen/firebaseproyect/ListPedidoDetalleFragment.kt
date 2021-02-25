package com.examen.firebaseproyect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterCompra
import com.examen.firebaseproyect.Adapter.AdapterPedidoDetalle
import com.examen.firebaseproyect.Clases.Compra
import com.examen.firebaseproyect.Clases.Pedido
import com.examen.firebaseproyect.Clases.Ubicacion
import com.examen.firebaseproyect.utilidades.Conexion
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class ListPedidoDetalleFragment : Fragment() {
    private lateinit var rc_compra: RecyclerView
    private lateinit var txt_total_compra: TextView
    private var adapterPedidoDetalle = AdapterPedidoDetalle()
    lateinit var msDatabaseReference: DatabaseReference
    var listCompras:ArrayList<Compra> = ArrayList()
    var idPedido:String?=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        idPedido= arguments?.getString("IdPedido")
        var view= inflater.inflate(R.layout.fragment_list_pedido_detalle, container, false)
        init(view)
        return view
    }fun init(view:View)
    {
        msDatabaseReference= FirebaseDatabase.getInstance().getReference()
        rc_compra=view.findViewById(R.id.rc_compra)
        txt_total_compra=view.findViewById(R.id.txt_total_compra)

        rc_compra.setHasFixedSize(true)
        rc_compra.layoutManager= LinearLayoutManager(requireContext())
        get_compras()


    }

    fun get_compras() {
        msDatabaseReference!!.child("PedidoDetalle").orderByChild("idPedido").equalTo(idPedido)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listCompras.clear()

                            for (postSnapshot in snapshot.children) {
                               val compra= postSnapshot.getValue(Compra::class.java)!!
                                listCompras.add(compra)
                            }
                            adapterPedidoDetalle.data(listCompras,requireContext(),msDatabaseReference,txt_total_compra)
                            rc_compra.adapter=adapterPedidoDetalle
                            var suma=0.0f
                            for(compra in listCompras)
                            {
                                suma=suma+compra.subtotal.toFloat()
                            }
                            txt_total_compra.text="Total:"+suma.toString()+"$"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )



    }


}