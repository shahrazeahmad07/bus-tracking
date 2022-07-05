package com.example.bustracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityRouteScheduleBinding
import com.google.firebase.database.*

class RouteSchedule : AppCompatActivity() {
    private lateinit var binding: ActivityRouteScheduleBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_schedule)
        binding = ActivityRouteScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Route Schedule"

        databaseReference = FirebaseDatabase.getInstance().getReference("RouteSchedule")
        databaseReference.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.tvRouteSchedule.text = snapshot.value as String
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}