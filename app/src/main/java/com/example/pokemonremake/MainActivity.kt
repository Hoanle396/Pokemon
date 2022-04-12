package com.example.pokemonremake

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.GoogleMap
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.lang.Exception

class MainActivity : FragmentActivity(), OnMapReadyCallback {
    var map: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        checkPermission()
        LoadPockemon()
    }
var ACCESSLOCATION =123
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }
        GetUserLocation()
    }

    fun GetUserLocation(){
        Toast.makeText(this,"User location access on",Toast.LENGTH_LONG).show()
        var myLocation= MylocationListener()

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30,3f,myLocation)
        var myThread= myThread()
        myThread.start()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESSLOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(this,"We can't access to your location ",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


    }

    var location:Location?=null

    //Get user location

    inner class MylocationListener:LocationListener{


        constructor(){
            location= Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation:Location?=null
    inner class myThread:Thread{

        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run(){

            while (true){

                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation=location


                    runOnUiThread {


                        map!!.clear()

                        // show me
                        val height = 120
                        val width = 120
                        val bitmap =
                            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.mc)
                        val icon = Bitmap.createScaledBitmap(bitmap, width, height, false)
                        val Danang = LatLng(location!!.latitude, location!!.longitude)
                        map!!.addMarker(
                            MarkerOptions()
                                .position(Danang)
                                .title("Me")
                                .snippet("here is my location")
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        )
                        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(Danang, 12f))

                        // show pockemons

                        for(i in 0..listPockemons.size-1){

                            var newPockemon=listPockemons[i]

                            if(newPockemon.IsCatch==false){
                                val bitmap =
                                    BitmapFactory.decodeResource(applicationContext.resources, newPockemon.image!!)
                                val icon = Bitmap.createScaledBitmap(bitmap, width, height, false)
                                val Danang = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)
                                map!!.addMarker(
                                    MarkerOptions()
                                        .position(Danang)
                                        .title(newPockemon.name)
                                        .snippet(newPockemon.des)
                                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                )
                                map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(Danang, 12f))

                                if (location!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.IsCatch=true
                                    listPockemons[i]=newPockemon
                                    playerPower+=newPockemon.power!!
                                    Toast.makeText(applicationContext,
                                        "You catch new pockemon your new pwoer is " + playerPower,
                                        Toast.LENGTH_LONG).show()

                                }

                            }
                        }

                    }
                    Thread.sleep(1000)

                }catch (ex:Exception){}

            }

        }

    }
    var playerPower=0.0
    var listPockemons=ArrayList<Pockemon>()

    fun  LoadPockemon(){


        listPockemons.add(Pockemon(R.drawable.pngwing2,
            "Charmander", "Charmander living in japan", 55.0, 37.7789994893035, -122.401846647263))
        listPockemons.add(Pockemon(R.drawable.pngwing3,
            "Bulbasaur", "Bulbasaur living in usa", 90.5, 37.7949568502667, -122.410494089127))
        listPockemons.add(Pockemon(R.drawable.pngwing4,
            "Squirtle", "Squirtle living in iraq", 33.5, 37.7816621152613, -122.41225361824))

    }
}

