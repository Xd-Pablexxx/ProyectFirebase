package com.examen.firebaseproyect

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.examen.firebaseproyect.Clases.Pedido
import com.examen.firebaseproyect.Clases.Ubicacion
import com.examen.firebaseproyect.utilidades.Conexion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.util.*


class UbicacionPedidoAdminFragment : Fragment(), OnMapReadyCallback{




    private lateinit var map: GoogleMap




    //
    private lateinit var msDatabaseReference: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_ubicacion_pedido_admin, container, false)
        initialize(root)
        var mapFragment: SupportMapFragment? = null
        mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return root
    }
    fun initialize(root: View)
    {

        msDatabaseReference= FirebaseDatabase.getInstance().getReference()

    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!

        msDatabaseReference.child("Pedido").addListenerForSingleValueEvent(
            object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {

                        for(postsnapshot in snapshot.children) {
                            val pedido = postsnapshot.getValue(Pedido::class.java)!!
                            msDatabaseReference.child("Ubicacion").orderByChild("key").equalTo(pedido.keyUbicacion).addListenerForSingleValueEvent(
                                object :ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists())
                                        {

                                            for(postsnapshot in snapshot.children)
                                            {
                                                val ubicacion=postsnapshot.getValue(Ubicacion::class.java)!!
                                                val sydney = LatLng(ubicacion.Latitud.toDouble(),ubicacion.Longitud.toDouble())

                                                map.addMarker(MarkerOptions().position(sydney).title("User:"+pedido.keyUsuario))

                                                map.setOnMarkerClickListener { marker ->Toast.makeText(requireContext(),"Pedido",Toast.LENGTH_SHORT).show();

                                                    false
                                                }
                                            }

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

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

    }




}