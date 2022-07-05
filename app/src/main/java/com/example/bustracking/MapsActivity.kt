package com.example.bustracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var mLongitude = 71.0
    private var mLatitude = 31.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Bus: abc"


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations")
        databaseReference.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

//                var dLatitude = snapshot.child("abc").child("longitude").value.toString().substring(1, snapshot.child("abc").child("longitude").value.toString().length-1)
                mLatitude = snapshot.child("abc").child("latitude").value as Double
                mLongitude = snapshot.child("abc").child("longitude").value as Double

                val latLng = LatLng(mLatitude, mLongitude)
                mMap.addMarker(MarkerOptions().position(latLng).title(""))
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latLng, 12.0f
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
    }
}

