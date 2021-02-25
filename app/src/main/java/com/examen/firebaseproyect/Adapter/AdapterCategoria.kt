package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Categoria
import com.examen.firebaseproyect.R

class AdapterCategoria:RecyclerView.Adapter<AdapterCategoria.ViewHolder>(){
    private var Datos:ArrayList<Categoria> = ArrayList()
    private lateinit var context:Context
    fun data(Datos:ArrayList<Categoria>, context: Context)
    {
        this.Datos=Datos
        this.context=context
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txt_nombre_categoria=itemView.findViewById(R.id.txt_nombre_categoria) as TextView
        var img_categoria=itemView.findViewById(R.id.img_categoria) as ImageView
        var txt_descripcion_categoria=itemView.findViewById(R.id.txt_descripcion_categoria) as TextView

        var layout_categoria=itemView.findViewById(R.id.layout_categoria) as LinearLayout
        fun holder(categoria:Categoria,context: Context){
            txt_nombre_categoria.text=categoria.nombre
            txt_descripcion_categoria.text=categoria.descripcion
            Glide.with(context).load(categoria.img).into(img_categoria)
            layout_categoria.setOnClickListener {
                var bundle=Bundle()
                bundle.putString("keyCategoria",categoria.key)
                Navigation.findNavController(it).navigate(R.id.listProductoAdminFragment,bundle)
            }
            layout_categoria.setOnLongClickListener {
                var bundle=Bundle()
                bundle.putSerializable("Categoria",categoria)
                Navigation.findNavController(it).navigate(R.id.udpateCategoriaAdminFragment,bundle)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCategoria.ViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.row_categoria_admin,parent,false)
        var viewHolder=ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: AdapterCategoria.ViewHolder, position: Int) {
       holder.holder(Datos.get(position),context)
    }

    override fun getItemCount(): Int = Datos.size
}