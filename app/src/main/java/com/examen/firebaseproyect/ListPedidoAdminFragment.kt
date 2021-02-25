package com.examen.firebaseproyect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.firebaseproyect.Adapter.AdapterPedido
import com.examen.firebaseproyect.Adapter.AdapterPedidoAdmin
import com.examen.firebaseproyect.Clases.Pedido
import com.examen.firebaseproyect.utilidades.Conexion
import com.google.firebase.database.*

class ListPedidoAdminFragment : Fragment() {
    private lateinit var rc_pedido: RecyclerView
    private lateinit var msDatabaseReference: DatabaseReference
    private var ls_pedidos:ArrayList<Pedido> = ArrayList()
    private var adapterPedido: AdapterPedidoAdmin = AdapterPedidoAdmin()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      var root= inflater.inflate(R.layout.fragment_list_pedido_admin, container, false)
            init(root)
            return root
    }
    fun init(view :View )
    {
        rc_pedido=view.findViewById(R.id.rc_pedido)
        msDatabaseReference= FirebaseDatabase.getInstance().getReference()
        rc_pedido.layoutManager = LinearLayoutManager(requireContext())
        get_pedido()
    }
    fun get_pedido()
    {

        msDatabaseReference.child("Pedido").addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        ls_pedidos.clear()
                        for(postsnapshot in snapshot.children)
                        {
                            val pedido=postsnapshot.getValue(Pedido::class.java)!!
                            ls_pedidos.add(pedido)
                        }
                        adapterPedido.data(requireContext(),ls_pedidos,msDatabaseReference)
                        rc_pedido.adapter=adapterPedido
                        adapterPedido.notifyDataSetChanged()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

    }


}