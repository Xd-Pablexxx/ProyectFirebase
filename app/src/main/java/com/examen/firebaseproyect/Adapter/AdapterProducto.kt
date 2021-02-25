package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.R
import com.google.android.material.textfield.TextInputEditText

class AdapterProducto: RecyclerView.Adapter<AdapterProducto.ViewHolder>(){
    private var Datos:ArrayList<Producto> = ArrayList()
    private lateinit var context: Context
    fun data(Datos:ArrayList<Producto>, context: Context)
    {
        this.Datos=Datos
        this.context=context
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txt_nombre_producto=itemView.findViewById(R.id.txt_nombre_producto) as TextView
        var img_producto=itemView.findViewById(R.id.img_producto) as ImageView
        var txt_precio_producto=itemView.findViewById(R.id.txt_precio_producto) as TextView
        var txt_descripcion_producto=itemView.findViewById(R.id.txt_descripcion_producto) as TextView
        //aumentar y disminuir
        var layout_producto=itemView.findViewById(R.id.layout_producto) as LinearLayout
        fun holder(producto: Producto, context: Context) {
            txt_nombre_producto.text = producto.nombre
            txt_precio_producto.text = "Precio:"+producto.precio
            txt_descripcion_producto.text="Descripcion: "+producto.descripcion

            Glide.with(context).load(producto.img).into(img_producto)
            layout_producto.setOnClickListener {
                var bundle= Bundle()
                bundle.putSerializable("Producto",producto)
                Navigation.findNavController(it).navigate(R.id.updateProductoAdminFragmnet,bundle)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterProducto.ViewHolder {
        var view=
            LayoutInflater.from(parent.context).inflate(R.layout.row_producto_admin,parent,false)
        var viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AdapterProducto.ViewHolder, position: Int) {
        holder.holder(Datos.get(position),context)
    }

    override fun getItemCount(): Int = Datos.size
}