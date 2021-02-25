package com.examen.firebaseproyect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Compra
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.Clases.UsuarioProducto
import com.examen.firebaseproyect.R
import com.examen.firebaseproyect.utilidades.Conexion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class AdapterCompra: RecyclerView.Adapter<AdapterCompra.ViewHolder>(){
    private var Datos:ArrayList<Compra> = ArrayList()
    private lateinit var context: Context
    private lateinit var msDataReference:DatabaseReference
    private lateinit var txt_total_compra: TextView
    fun data(Datos:ArrayList<Compra>, context: Context,msDatabaseReference: DatabaseReference,txt_total_compra:TextView)
    {
        this.Datos=Datos
        this.context=context
        this.msDataReference=msDatabaseReference
        this.txt_total_compra=txt_total_compra
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txt_nombre_producto=itemView.findViewById(R.id.txt_nombre_producto) as TextView
        var img_producto=itemView.findViewById(R.id.img_producto) as ImageView
        var txt_precio_producto=itemView.findViewById(R.id.txt_precio_producto) as TextView
        var txt_cantidad_producto=itemView.findViewById(R.id.txt_cantidad_producto) as TextView
        var txt_costo_producto=itemView.findViewById(R.id.txt_costo_producto) as TextView
        //aumentar y disminuir
        var btn_aumentar_compra=itemView.findViewById(R.id.btn_aumentar_compra) as Button
        var btn_disminuir_compra=itemView.findViewById(R.id.btn_disminuir_compra) as Button
        //

        var product_car=itemView.findViewById(R.id.producto_card) as LinearLayout
        var cardvisible=itemView.findViewById(R.id.card_visible) as LinearLayout
        var cantidad:Float=0.0f
        var precio:Float=0.0f
        fun holder(compra: Compra, context: Context,msDatabaseReference: DatabaseReference,txt_total_compra: TextView){
            cantidad=compra.cantidad.toFloat()
            btn_aumentar_compra.setOnClickListener {
                cantidad=cantidad+1
                var ct=cantidad*precio
                txt_costo_producto.text="COSTO:"+ct.toString()
                txt_cantidad_producto.text="CANTIDAD:"+cantidad.toString()
                var db= Conexion().bd(context)
                db.execSQL("update Compra set subtotal='${ct}',cantidad='${cantidad}' where idproducto='${compra.idProducto}'")
                var cursor=db.rawQuery("select sum(subtotal) from Compra",null)
                cursor.moveToFirst()
                txt_total_compra.text="TOTAL:"+cursor.getString(0).toFloat().toString()+"$"
                db.close()

            }
            btn_disminuir_compra.setOnClickListener {
                if(cantidad!=0.0f)
                {
                    cantidad=cantidad-1
                    var ct=cantidad*precio
                    txt_costo_producto.text="COSTO:"+ct.toString()
                    txt_cantidad_producto.text="CANTIDAD:"+cantidad.toString()
                    var db= Conexion().bd(context)
                    db.execSQL("update Compra set subtotal='${ct}',cantidad='${cantidad}' where idproducto='${compra.idProducto}'")
                    var cursor=db.rawQuery("select sum(subtotal) from Compra",null)
                    cursor.moveToFirst()
                    txt_total_compra.text="TOTAL:"+cursor.getString(0).toFloat().toString()+"$"
                    db.close()
                }

            }
            txt_cantidad_producto.setText("CANTIDAD:"+compra.cantidad.toFloat().toString())
            product_car.setOnClickListener {
                if (cardvisible.isVisible) {
                    TransitionManager.beginDelayedTransition(
                        product_car,
                        AutoTransition()
                    )
                    cardvisible.setVisibility(View.GONE)
                } else {

                    TransitionManager.beginDelayedTransition(
                        product_car,
                        AutoTransition()
                    )
                    cardvisible.setVisibility(View.VISIBLE)
                }
            }
            msDatabaseReference.child("Producto").orderByChild("key").equalTo(compra.idProducto).addValueEventListener(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                            var producto=Producto()
                                for (postSnapshot in snapshot.children) {
                                    producto = postSnapshot.getValue(Producto::class.java)!!
                                }
                            txt_nombre_producto.text=producto.nombre
                            txt_precio_producto.text="PRECIO:"+producto.precio
                            precio=producto.precio.toFloat()
                            txt_costo_producto.text="COSTO:"+(producto.precio.toFloat()*compra.cantidad.toFloat()).toString()
                            Glide.with(context).load(producto.img).into(img_producto)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
            )


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCompra.ViewHolder {
        var view=
            LayoutInflater.from(parent.context).inflate(R.layout.row_compra_producto,parent,false)
        var viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AdapterCompra.ViewHolder, position: Int) {
        holder.holder(Datos.get(position),context,msDataReference,txt_total_compra)
    }

    override fun getItemCount(): Int = Datos.size
}