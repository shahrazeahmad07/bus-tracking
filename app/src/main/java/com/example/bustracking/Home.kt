package com.example.bustracking

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bustracking.databinding.ActivityHomeBinding
import com.example.bustracking.modals.RVBusDriverModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Home : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private var sharingLocation = false
    private lateinit var databaseReference: DatabaseReference

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager

    private var mLongitude = 0.0
    private var mLatitude = 0.0
//    private late init var mFusedLocationClient: FusedLocationProviderClient
//    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
//    private var mLongitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Driver Home"
        mAuth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.getReference("Locations")

        sharingLocation = false
        checkLocationSharing()

//        //! location listener attachment.
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


        binding.btnShareLocation.setOnClickListener {
            sharingLocation = !sharingLocation
            checkLocationSharing()
            if (sharingLocation) {
                if (!isLocationEnabled()) {
                    Toast.makeText(
                        this,
                        "Your location provider is turned off. Please turn it on.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // For Getting current location of user please have a look at below link for better understanding
                    // https://www.androdocs.com/kotlin/getting-current-location-latitude-longitude-in-android-using-kotlin.html
                    Dexter.withActivity(this)
                        .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                if (report!!.areAllPermissionsGranted()) {
                                    try {
                                        if (ActivityCompat.checkSelfPermission(
                                                applicationContext,
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                                applicationContext,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            return
                                        }
                                        locationManager.requestLocationUpdates(
                                            LocationManager.NETWORK_PROVIDER,
                                            5000L,
                                            5f,
                                            locationListener
                                        )
                                        locationManager.requestLocationUpdates(
                                            LocationManager.GPS_PROVIDER,
                                            5000L,
                                            5f,
                                            locationListener
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>?,
                                token: PermissionToken?
                            ) {
                                showRationalDialogForPermissions()
                            }
                        }).onSameThread()
                        .check()
                }


            } else {
                locationManager.removeUpdates(locationListener)
            }
        }

        locationListener = LocationListener { location ->
            try {
                mLongitude = location.longitude
                mLatitude = location.latitude

                val username = mAuth.currentUser?.email.toString()
                val partsOfEmail = username.split('@')

                val rvBusDriverModal = RVBusDriverModal(partsOfEmail[0], mLongitude, mLatitude)
                databaseReference.child(partsOfEmail[0]).setValue(rvBusDriverModal)
                //databaseReference.child(partsOfEmail[0]).child("latitude").setValue(mLatitude)
                //databaseReference.child(partsOfEmail[0]).child("longitude").setValue(mLongitude)
            } catch (e: Exception) {

            }
        }


    }


    //    //! Get Current Location
//    private fun getCurrentLocation() {
//        if (!isLocationEnabled()) {
//            Toast.makeText(
//                this,
//                "Your location provider is turned off. Please turn it on.",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            // For Getting current location of user please have a look at below link for better understanding
//            // https://www.androdocs.com/kotlin/getting-current-location-latitude-longitude-in-android-using-kotlin.html
//            Dexter.withActivity(this)
//                .withPermissions(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//                .withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                        if (report!!.areAllPermissionsGranted()) {
//
//                            requestNewLocationData()
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        permissions: MutableList<PermissionRequest>?,
//                        token: PermissionToken?
//                    ) {
//                        showRationalDialogForPermissions()
//                    }
//                }).onSameThread()
//                .check()
//        }
//    }
//
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
//
//    private fun requestNewLocationData() {
//
//        val mLocationRequest = LocationRequest()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {

    //            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        mFusedLocationClient.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val mLastLocation: Location? = locationResult.lastLocation
//            if (mLastLocation != null) {
//                mLatitude = mLastLocation.latitude
//            }
//            Log.e("Current Latitude", "$mLatitude")
//            if (mLastLocation != null) {
//                mLongitude = mLastLocation.longitude
//            }
//            Log.e("Current Longitude", "$mLongitude")
//
////
////            // START
////            val addressTask =
////                GetAddressFromLatLng(this@Home, mLatitude, mLongitude)
////
////            addressTask.setAddressListener(object :
////                GetAddressFromLatLng.AddressListener {
////                override fun onAddressFound(address: String?) {
////                    Log.e("Address ::", "" + address)
////                    //?et_location.setText(address) // Address is set to the edittext
////                }
////
////                override fun onError() {
////                    Log.e("Get Address ::", "Something is wrong...")
////                }
////            })
////
////            addressTask.getAddress()
//            // END
//        }
//    }
//
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }


    //! checking sharing location status
    private fun checkLocationSharing() {
        if (!sharingLocation) {
            binding.tvLocationStatus.text = getString(R.string.negative_location_status)
            binding.btnShareLocation.text = getString(R.string.share_location)
        } else {
            binding.tvLocationStatus.text = getString(R.string.positive_location_status)
            binding.btnShareLocation.text = getString(R.string.stop_sharing)
        }
    }

    //! menu Logout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mLogout) {
            Toast.makeText(this, "You are Logged Out", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}