package com.examen.firebaseproyect

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PrimerRegistroFragment : Fragment(), View.OnClickListener, TextWatcher {
   private lateinit var txt_usuario:TextInputEditText
    private lateinit var txt_password:TextInputEditText
    private lateinit var txt_confirm_password:TextInputEditText
    private lateinit var btn_next:Button

    // Layout text_EditText
    private lateinit var TL_usuario:TextInputLayout
    private lateinit var TL_password:TextInputLayout
    private lateinit var TL_confirm_password:TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      var view=inflater.inflate(R.layout.fragment_primer_registro, container, false)
        init(view)
        return view
    }
    fun init(view: View)
    {
        txt_usuario=view.findViewById(R.id.txt_usuario)
        txt_password=view.findViewById(R.id.txt_password)
        txt_confirm_password=view.findViewById(R.id.txt_confirm_password)
        TL_usuario=view.findViewById(R.id.TL_usuario)
        TL_password=view.findViewById(R.id.TL_password)
        TL_confirm_password=view.findViewById(R.id.TL_confirm_password)
        txt_usuario.addTextChangedListener(this)
        txt_password.addTextChangedListener(this)
        txt_confirm_password.addTextChangedListener(this)
        btn_next=view.findViewById(R.id.btn_next)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        var id=p0?.id
        when(id)
        {
            R.id.btn_next->
            {
                if(txt_usuario.text.toString()=="")
                {
                    TL_usuario.error="Datos vacio"
                }
                else if(txt_password.text.toString()=="")
                {
                    TL_password.error="Datos vacio"
                }
                else if(txt_confirm_password.text.toString()=="")
                {
                    TL_confirm_password.error="Datos vacio"
                }
                else
                {
                    if(txt_password.text.toString()!=txt_confirm_password.text.toString())
                    {
                        Toast.makeText(this.requireContext(),"No coinciden las Contraseñas",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        val fragmentManager=getFragmentManager()
                        val fragmentTransaction= fragmentManager?.beginTransaction()
                        val fragment= SegundoRegistroFragment()
                        val bundle:Bundle= Bundle()
                        bundle.putString("Username",txt_usuario.text.toString())
                        bundle.putString("Password",txt_password.text.toString())
                        fragment.arguments=bundle
                        fragmentTransaction?.replace(R.id.fragment_container_firstactivity,fragment)
                            ?.addToBackStack(null)
                        fragmentTransaction?.commit()
                    }

                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        TL_usuario.isErrorEnabled=false
        TL_password.isErrorEnabled=false
        TL_confirm_password.isErrorEnabled=false
    }

}