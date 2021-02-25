package com.examen.firebaseproyect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.examen.firebaseproyect.utilidades.Conexion

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val fragmentManager = supportFragmentManager;
        val fragmentTransaction = fragmentManager.beginTransaction()
        var db= Conexion().bd(this)
        var cursor=db.rawQuery("Select * from usuario",null)
        if(cursor.count==0)
        {
            val fragment = LoginFragment()
            fragmentTransaction.add(R.id.fragment_container_firstactivity, fragment)
            fragmentTransaction.commit()
        }
        else{

            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        db.close()


    }


}