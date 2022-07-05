package com.example.bustracking

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bustracking.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager

    private var mLongitude = 0.0
    private var mLatitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations")
        databaseReference.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        ActivityCompat.requestPermissions(this, [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION], )


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        locationListener = LocationListener { location ->
            try {
                mLongitude = location.longitude
                mLatitude = location.latitude

                val username = mAuth.currentUser?.email.toString()
                val partsOfEmail = username.split('@')
                databaseReference.child(partsOfEmail[0]).child("latitude").push().setValue(mLatitude)
                databaseReference.child(partsOfEmail[0]).child("longitude").push().setValue(mLongitude)
            }catch (e: Exception) {

            }
        }



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 5f, locationListener)
        }catch (e: Exception) {

        }
    }
}