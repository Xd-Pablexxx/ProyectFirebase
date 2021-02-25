package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Pedido
import com.examen.firebaseproyect.Clases.Ubicacion
import com.examen.firebaseproyect.R
import com.google.firebase.database.*

class AdapterPedido: RecyclerView.Adapter<AdapterPedido.MyViewHolder>() {
    private lateinit var context:Context
    private  var data:MutableList<Pedido> = ArrayList()
    private lateinit var msDataReference:DatabaseReference
    fun data(context: Context, data: MutableList<Pedido>,msDataReference:DatabaseReference)
    {
        this.data=data
        this.context=context
        this.msDataReference=msDataReference
    }
    class MyViewHolder(view: View) :RecyclerView.ViewHolder(view){


        val FechaCreacion=view.findViewById(R.id.Fecha) as TextView
        val EstadoPedido=view.findViewById(R.id.EstadoPedido) as TextView
        val Direccion=view.findViewById(R.id.Direccion) as TextView
        var contenedor=view.findViewById(R.id.contenedor) as CardView
        var id=""

        fun bind(pedido: Pedido,context: Context,msDataReference:DatabaseReference)
        {

            id=pedido.key

            FechaCreacion.text="Creacion:"+pedido.fecha
            if(pedido.estado=="0")
                EstadoPedido.text="Estado: No Entregado"
            else
                EstadoPedido.text="Estado: Entregado"

            msDataReference.child("Ubicacion").orderByChild("key").equalTo(pedido.keyUbicacion).addListenerForSingleValueEvent(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists())
                       {
                           var ubicacion=Ubicacion()
                           for(postSnapshot in snapshot.children)
                           {
                            ubicacion= postSnapshot.getValue(Ubicacion::class.java)!!
                           }
                           Direccion.text="Direccion:"+ubicacion.Calle
                       }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
            contenedor.setOnClickListener{
                var bundle=Bundle()
                bundle.putString("IdPedido",id)
                Navigation.findNavController(it).navigate(R.id.listPedidoDetalleFragment,bundle)
            }
        }



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.row_pedido, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pedido: Pedido =data.get(position)
        holder.bind(pedido,context,msDataReference)
    }

    override fun getItemCount()= data.size
}