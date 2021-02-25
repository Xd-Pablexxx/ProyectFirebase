package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.Clases.UsuarioProducto
import com.examen.firebaseproyect.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


class AdapterProductoCliente: RecyclerView.Adapter<AdapterProductoCliente.ViewHolder>(){
    private var Datos:ArrayList<Producto> = ArrayList()
    private lateinit var context: Context
    private lateinit var view_container:View
    private lateinit var msDatabaseReference: DatabaseReference
    private lateinit var id:String
    fun data(Datos:ArrayList<Producto>, context: Context,view:View,msDatabaseReference: DatabaseReference,id:String)
    {
        this.Datos=Datos
        this.context=context
        this.view_container=view
        this.msDatabaseReference=msDatabaseReference
        this.id=id
    }
    class ViewHolder(itemView: View,view: View,msDatabaseReference: DatabaseReference,id:String): RecyclerView.ViewHolder(itemView) {

        var img_producto=itemView.findViewById(R.id.img_producto) as ImageView
        var img_producto_container=view.findViewById(R.id.img_producto_container) as ImageView
        var txt_nombre_producto=view.findViewById(R.id.txt_nombre_producto) as TextView
        var txt_precio_producto=view.findViewById(R.id.txt_precio_producto) as TextView
        var txt_descripcion_producto=view.findViewById(R.id.txt_descripcion_producto) as TextView
        var pgrb_producto=view.findViewById(R.id.ratingBar_producto) as RatingBar
        var msDatabaseReference=msDatabaseReference
        var id=id

        fun holder(producto: Producto, context: Context){

                Glide.with(context).load(producto.img).into(img_producto)
             /*   img_producto_container.setOnClickListener {
                    var bundle=Bundle()
                    bundle.putSerializable("Producto",producto)
                    Navigation.findNavController(it).navigate(R.id.compraProductoFragment,bundle)
                }*/

                img_producto.setOnLongClickListener{
                    var bundle=Bundle()
                    bundle.putSerializable("Producto",producto)
                    Navigation.findNavController(it).navigate(R.id.compraProductoFragment,bundle)
                    false
                }


                img_producto.setOnClickListener {
                img_producto_container.visibility=View.VISIBLE
                Glide.with(context).load(producto.img).into(img_producto_container)
                txt_nombre_producto.visibility=View.VISIBLE
                txt_precio_producto.visibility=View.VISIBLE

                txt_descripcion_producto.visibility=View.VISIBLE
                txt_nombre_producto.text=producto.nombre
                txt_precio_producto.text=producto.precio.toFloat().toString()+"$"
                txt_descripcion_producto.text=producto.descripcion

                pgrb_producto.visibility=View.VISIBLE

                msDatabaseReference!!.child("UsuarioProducto").orderByChild("keyUsuarioProducto").
                equalTo(id+producto.key).addListenerForSingleValueEvent(
                    object : ValueEventListener
                    {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                            {
                                var usuarioProducto=UsuarioProducto()
                                for (postSnapshot in snapshot.children) {
                                    usuarioProducto = postSnapshot.getValue(UsuarioProducto::class.java)!!
                                }
                                pgrb_producto.rating=usuarioProducto.puntuacion.toFloat()

                            }
                            else
                            {
                                pgrb_producto.rating="0".toFloat()
                                val keyProducto= producto.key
                                val keyUsuario=id
                                val id = msDatabaseReference!!.push().key
                                val mProduct = UsuarioProducto(
                                    id!!,keyUsuario,keyProducto,(keyUsuario+keyProducto),"0"
                                )
                                if (id != null) {
                                    msDatabaseReference!!.child("UsuarioProducto").child(id).setValue(mProduct)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }

                )
                pgrb_producto.setOnRatingBarChangeListener{ ratingBar, fl, b ->
                    msDatabaseReference!!.child("UsuarioProducto").orderByChild("keyUsuarioProducto").
                    equalTo(id+producto.key).addListenerForSingleValueEvent(
                        object : ValueEventListener
                        {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists())
                                {
                                    var usuarioProducto=UsuarioProducto()
                                    for (postSnapshot in snapshot.children) {
                                        usuarioProducto = postSnapshot.getValue(UsuarioProducto::class.java)!!
                                    }
                                    var id=usuarioProducto.key
                                    usuarioProducto.puntuacion=fl.toString()
                                    msDatabaseReference!!.child("UsuarioProducto").child(id).setValue(usuarioProducto)


                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        }

                    )
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterProductoCliente.ViewHolder {
        var view=
            LayoutInflater.from(parent.context).inflate(R.layout.row_producto,parent,false)
        var viewHolder=ViewHolder(view,view_container,msDatabaseReference,id)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AdapterProductoCliente.ViewHolder, position: Int) {
        holder.holder(Datos.get(position),context)
    }

    override fun getItemCount(): Int = Datos.size
}