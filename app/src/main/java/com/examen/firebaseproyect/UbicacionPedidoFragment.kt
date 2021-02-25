package com.examen.firebaseproyect

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.examen.firebaseproyect.Clases.Ubicacion
import com.examen.firebaseproyect.utilidades.Conexion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import java.util.*


class UbicacionPedidoFragment : Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private  var key:Boolean=true
    private lateinit var map: GoogleMap
    private  var  mark: Marker?=null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private var calle:String=""
    private lateinit var geocoder: Geocoder
    private lateinit var layout_guardar_ubicacion:LinearLayout
    private lateinit var layout_mostrar_ubicacion:LinearLayout
    private lateinit var layout_ubicacion_mostrar:LinearLayout
    //
    private lateinit var msDatabaseReference: DatabaseReference
    private lateinit var txt_latitud:TextView
    private lateinit var txt_longitud:TextView
    private lateinit var txt_calle:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var root= inflater.inflate(R.layout.fragment_ubicacion_pedido, container, false)
        initialize(root)
        var mapFragment: SupportMapFragment? = null
        mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return root
    }
    fun initialize(root: View)
    {
        geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        msDatabaseReference=FirebaseDatabase.getInstance().getReference()
        layout_guardar_ubicacion=root.findViewById(R.id.layout_guardar_ubicacion)
        layout_mostrar_ubicacion=root.findViewById(R.id.layout_mostrar_ubicacion)
        layout_ubicacion_mostrar=root.findViewById(R.id.layout_ubicacion_mostrar)
        //TextViews
        txt_latitud=root.findViewById(R.id.txt_latitud)
        txt_longitud=root.findViewById(R.id.txt_longitud)
        txt_calle=root.findViewById(R.id.txt_calle)

        //
        layout_guardar_ubicacion.setOnClickListener {
            var db=Conexion().bd(requireContext())
            var cursor=db.rawQuery("select * from Usuario", null)
            cursor.moveToFirst()
            var idUsuario=cursor.getString(0)
            db.close()
            msDatabaseReference!!.child("Ubicacion").orderByChild("idUsuario").equalTo(idUsuario).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            var ubicacion = Ubicacion()
                            for (postSnapshot in snapshot.children) {
                                ubicacion = postSnapshot.getValue(Ubicacion::class.java)!!
                            }
                            var id = ubicacion.key
                            ubicacion = Ubicacion(
                                id.toString(),
                                idUsuario,
                                latitude.toString(),
                                longitude.toString(),
                                calle
                            )
                            msDatabaseReference.child("Ubicacion").child(id.toString()).setValue(
                                ubicacion
                            )


                        } else {
                            var id = msDatabaseReference.push().key
                            var ubicacion = Ubicacion(
                                id.toString(),
                                idUsuario,
                                latitude.toString(),
                                longitude.toString(),
                                calle
                            )
                            msDatabaseReference.child("Ubicacion").child(id.toString()).setValue(
                                ubicacion
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )






        }
        layout_mostrar_ubicacion.setOnClickListener {

            var db=Conexion().bd(requireContext())
            var cursor=db.rawQuery("select * from Usuario", null)
            cursor.moveToFirst()
            var idUsuario=cursor.getString(0)
            db.close()
            msDatabaseReference!!.child("Ubicacion").orderByChild("idUsuario").equalTo(idUsuario).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            layout_ubicacion_mostrar.visibility=View.VISIBLE
                            var ubicacion = Ubicacion()
                            for (postSnapshot in snapshot.children) {
                                ubicacion = postSnapshot.getValue(Ubicacion::class.java)!!
                            }
                            txt_latitud.text="Latitud:${ubicacion.Latitud}"
                            txt_longitud.text="Longitud:${ubicacion.Longitud}"
                            txt_calle.text="Calle:${ubicacion.Calle}"




                        } else {
                           Toast.makeText(requireContext(),"NO TIENE UNA UBICACION",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )



        }
        layout_ubicacion_mostrar.setOnClickListener {
            layout_ubicacion_mostrar.visibility=View.GONE
        }
    }
    fun getlocalizacion() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            );
        } else {
            locationStart();
        }
    }
    private fun locationStart() {
        val mlocManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val Local = object : LocationListener {

            override fun onLocationChanged(loc: Location) {
                // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
                // debido a la deteccion de un cambio de ubicacion
                if(key)
                {
                    mark?.remove()

                    mark=map.addMarker(
                        MarkerOptions()
                            .position(LatLng(loc.latitude, loc.longitude))
                            .title("YO").draggable(true)
                    )

                    val list: List<Address> = geocoder.getFromLocation(
                        loc.latitude, loc.longitude, 1

                    )
                    val DirCalle = list[0]
                    calle =DirCalle.getAddressLine(0).toString()

                    latitude=loc.latitude
                    longitude=loc.longitude
                    val miUbicacion = LatLng(latitude, longitude)

                    map.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                    val cameraPosition = CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(14f)
                        .bearing(90f)
                        .tilt(45f)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    key=false

                }




            }


            override fun onProviderDisabled(provider: String) {
               Log.i("posicion", "GPS desactivado")
            }

            override fun onProviderEnabled(provider: String) {
                Log.i("posicion", "GPS Activado")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                when (status) {
                    LocationProvider.AVAILABLE -> Log.d("debug", "LocationProvider.AVAILABLE")
                    LocationProvider.OUT_OF_SERVICE -> Log.d(
                        "debug",
                        "LocationProvider.OUT_OF_SERVICE"
                    )
                    LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d(
                        "debug",
                        "LocationProvider.TEMPORARILY_UNAVAILABLE"
                    )
                }
            }
        }

        val gpsEnabled = mlocManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
            return
        }
        mlocManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0.0F,
            Local as LocationListener
        )
        mlocManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0.0F,
            Local as LocationListener
        )
        Log.i("posicion", "localizacion Agregada")


    }
    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        map.setOnMarkerDragListener(this)
        var db=Conexion().bd(requireContext())
        var cursor=db.rawQuery("select * from Usuario", null)
        cursor.moveToFirst()
        var idUsuario=cursor.getString(0)
        db.close()
        msDatabaseReference!!.child("Ubicacion").orderByChild("idUsuario").equalTo(idUsuario).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        var ubicacion = Ubicacion()
                        for (postSnapshot in snapshot.children) {
                            ubicacion = postSnapshot.getValue(Ubicacion::class.java)!!
                        }
                        mark?.remove()

                        mark = map.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        ubicacion.Latitud.toDouble(),
                                        ubicacion.Longitud.toDouble()
                                    )
                                )
                                .title("YO").draggable(true)
                        )
                        latitude = ubicacion.Latitud.toDouble()
                        longitude = ubicacion.Longitud.toDouble()
                        calle = ubicacion.Calle
                        val miUbicacion = LatLng(latitude, longitude)

                        map.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                        val cameraPosition = CameraPosition.Builder()
                            .target(miUbicacion)
                            .zoom(14f)
                            .bearing(90f)
                            .tilt(45f)
                            .build()
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                        key=false


                    } else {
                        getlocalizacion()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


    }
    override  fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        var clickCount = marker.tag as Int?

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1
            marker.tag = clickCount
            Toast.makeText(
                this.context,
                marker.title +
                        " has been clicked " + clickCount + " times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        val list: List<Address> = geocoder.getFromLocation(
            p0?.position!!.latitude, p0?.position!!.longitude, 1

        )
        longitude=p0?.position!!.longitude
        latitude=p0?.position!!.latitude
        calle = list[0].getAddressLine(0)
        Log.i("infor", latitude.toString() + " e " + longitude.toString())
    }


}