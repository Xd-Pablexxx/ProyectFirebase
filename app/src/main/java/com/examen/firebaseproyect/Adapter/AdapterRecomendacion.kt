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
import com.examen.firebaseproyect.Clases.Recomendacion
import com.examen.firebaseproyect.R

class AdapterRecomendacion:RecyclerView.Adapter<AdapterRecomendacion.ViewHolder>() {
    private var Datos:ArrayList<Recomendacion> = ArrayList()
    private lateinit var context: Context
    fun data(Datos:ArrayList<Recomendacion>, context: Context)
    {
        this.Datos=Datos
        this.context=context
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       // var txt_nombre_recomendacion=itemView.findViewById(R.id.txt_nombre_recomendacion) as TextView
        var img_recomendacion=itemView.findViewById(R.id.img_recomendacion) as ImageView
     //   var txt_descripcion_categoria=itemView.findViewById(R.id.txt_descripcion_categoria) as TextView

        var layout_recomendacion=itemView.findViewById(R.id.layout_recomendacion) as LinearLayout
        fun holder(recomendacion: Recomendacion, context: Context){
           // txt_nombre_recomendacion.text=categoria.nombre
          //  txt_descripcion_categoria.text=categoria.descripcion
            Glide.with(context).load(recomendacion.img).into(img_recomendacion)
            layout_recomendacion.setOnClickListener {
                var bundle=Bundle()
                bundle.putSerializable("Recomendacion",recomendacion)
                Navigation.findNavController(it).navigate(R.id.updateRecomendacionAdminFragment,bundle)
                true
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=
            LayoutInflater.from(parent.context).inflate(R.layout.row_recomendacion_admin,parent,false)
        var viewHolder=ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.holder(Datos.get(position),context)
    }

    override fun getItemCount(): Int = Datos.size
}