package com.examen.firebaseproyect

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.examen.firebaseproyect.Clases.Producto
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.google.android.material.textfield.TextInputEditText

class CompraProductoFragment : Fragment() {

    private lateinit var img_producto:ImageView
    private lateinit var txt_nombre_producto:TextView
    private lateinit var txt_precio_producto:TextView
    private lateinit var txt_cantidad_producto:TextView
    private lateinit var txt_subtotal_producto:TextView
    private lateinit var txt_sugerencia_producto:TextInputEditText
    //
    private lateinit var layout_agregar_carrito:LinearLayout
    //
    private var producto=Producto()
    //
    private lateinit var img_mas_producto:ImageView
    private lateinit var img_menos_producto:ImageView
    //
    private  var precio:Float=0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmen<t
        producto= arguments?.getSerializable("Producto") as Producto
        precio=producto.precio.toFloat()
        var view= inflater.inflate(R.layout.fragment_compra_producto, container, false)
        init(view)
        return view
    }
    fun init(view: View)
    {
        layout_agregar_carrito=view.findViewById(R.id.layout_agregar_carrito)
        img_producto=view.findViewById(R.id.img_producto)
        Glide.with(requireContext()).load(producto.img).into(img_producto)
        txt_nombre_producto=view.findViewById(R.id.txt_nombre_producto)
        txt_precio_producto=view.findViewById(R.id.txt_precio_producto)
        txt_cantidad_producto=view.findViewById(R.id.txt_cantidad_producto)
        txt_subtotal_producto=view.findViewById(R.id.txt_subtotal_producto)
        txt_sugerencia_producto=view.findViewById(R.id.txt_sugerencia_producto)
        txt_cantidad_producto.text="0"
        txt_nombre_producto.text=producto.nombre
        txt_precio_producto.text="Precio:"+producto.precio.toDouble().toString()+"$"
        img_mas_producto=view.findViewById(R.id.img_mas_producto)
        img_menos_producto=view.findViewById(R.id.img_menos_producto)
        img_mas_producto.setOnClickListener {
            txt_cantidad_producto.setText((txt_cantidad_producto.text.toString().toInt()+1).toString())
            txt_subtotal_producto.text=((txt_cantidad_producto.text.toString().toFloat())*precio).toString()
        }
        img_menos_producto.setOnClickListener {
            if(txt_cantidad_producto.text.toString().toInt()>0)
            {
                txt_cantidad_producto.setText((txt_cantidad_producto.text.toString().toInt()-1).toString())
                txt_subtotal_producto.text=((txt_cantidad_producto.text.toString().toFloat())*precio).toString()
            }
        }
        layout_agregar_carrito.setOnClickListener {

            var db=Conexion().bd(requireContext())
            var cursor=db.rawQuery("select * from Compra where idproducto='${producto.key}'",null)

            if(cursor.count>0)
            {
                Toast.makeText(requireContext(),"Ya Esta Agregado,Modificalo",Toast.LENGTH_LONG).show()
            }
            else
            {
                var values=ContentValues()
                values.put(Utilidades.CAMPO_IDPRODUCTO,producto.key)
                values.put(Utilidades.CAMPO_CANTIDAD,txt_cantidad_producto.text.toString())
                values.put(Utilidades.CAMPO_PRECIO,producto.precio)
                values.put(Utilidades.CAMPO_SUBTOTAL,(txt_cantidad_producto.text.toString().toFloat()*producto.precio.toFloat()).toString())
                values.put(Utilidades.CAMPO_SUGERENCIA,txt_sugerencia_producto.text.toString())
                db.insert(Utilidades.TABLA_COMPRA,null,values)
                Toast.makeText(requireContext(),"Compra Agregada",Toast.LENGTH_LONG).show()
            }

            db.close()



        }
    }


}