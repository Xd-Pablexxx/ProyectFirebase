package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class AdapterProductoClienteHome: RecyclerView.Adapter<AdapterProductoClienteHome.ViewHolder>(){
    private var Datos:ArrayList<String> = ArrayList()
    private lateinit var context: Context
    private lateinit var msDatabaseReference: DatabaseReference
    fun data(Datos:ArrayList<String>, context: Context,msDatabaseReference: DatabaseReference)
    {
        this.Datos=Datos
        this.context=context
        this.msDatabaseReference=msDatabaseReference
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txt_nombre_producto=itemView.findViewById(R.id.txt_nombre_producto) as TextView
        var img_producto=itemView.findViewById(R.id.img_producto) as CircleImageView
        var txt_precio_producto=itemView.findViewById(R.id.txt_precio_producto) as TextView
        var txt_cantidad_producto=itemView.findViewById(R.id.txt_cantidad_producto) as TextView
        //aumentar y disminuir
        fun holder(keyproducto:String, context: Context,msDatabaseReference: DatabaseReference){

            msDatabaseReference.child("Producto").orderByChild("key").equalTo(keyproducto).addValueEventListener(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                     if(snapshot.exists())
                     {
                         var producto=Producto()
                         for(postsnaphot in snapshot.children)
                         {
                             producto=postsnaphot.getValue(Producto::class.java)!!

                         }
                         txt_nombre_producto.text=producto.nombre
                         txt_precio_producto.text="Precio:"+producto.precio


                         Glide.with(context).load(producto.img).into(img_producto)

                     }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdapterProductoClienteHome.ViewHolder {
        var view=
            LayoutInflater.from(parent.context).inflate(R.layout.row_producto_home,parent,false)
        var viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AdapterProductoClienteHome.ViewHolder, position: Int) {
        holder.holder(Datos.get(position),context,msDatabaseReference)
    }

    override fun getItemCount(): Int = Datos.size
}