package com.examen.firebaseproyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examen.firebaseproyect.utilidades.Conexion
import com.example.koalito.Bd.utilidades.Utilidades
import com.example.koalito.Bd.utilidades.Utilidades.TABLA_USUARIOS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title=" "
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        var db= Conexion().bd(this)
        var cursor=db.rawQuery("Select * from ${TABLA_USUARIOS}",null)
        cursor.moveToFirst()
        if(cursor.getString(7)=="admin")
        {
            navView.getMenu().clear();
            navView.inflateMenu(R.menu.activity_main_drawer_admin);
        }
        db.close()


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home,R.id.homeAdminFragment,R.id.listPedidoAdminFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var usuario=nav_view.getHeaderView(0 ).findViewById(R.id.txt_usuario) as TextView
        var nombre=nav_view.getHeaderView(0).findViewById(R.id.txt_nombre_apellidos) as TextView
        var exit=nav_view.getHeaderView(0).findViewById(R.id.img_exit) as ImageView
        exit.setOnClickListener {
            var db=Conexion().bd(this)
            db.delete(Utilidades.TABLA_USUARIOS, null, null)
            db.delete(Utilidades.TABLA_COMPRA,null,null)
            db.delete(Utilidades.TABLA_UBICACION,null,null)
            db.close()
            val intent = Intent(this, FirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        view_nombre_usuario(usuario,nombre)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    fun view_nombre_usuario(usuario:TextView,nombre:TextView)
    {
        var db= Conexion().bd(this)
        var cursor=db.rawQuery("Select * from ${TABLA_USUARIOS}",null)
        cursor.moveToFirst()
        usuario.text=cursor.getString(1)
        nombre.text=cursor.getString(3)+" "+cursor.getString(4)
        db.close()

    }
}