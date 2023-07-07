package com.example.bustracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.AdminComplainAdapter
import com.example.bustracking.databinding.ActivityAdminComplaintBoxBinding
import com.example.bustracking.databinding.ActivityComplaintBoxBinding
import com.example.bustracking.modals.ComplainModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminComplaintBox : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var complainModalArrayList: ArrayList<ComplainModal>
    private lateinit var adminComplainAdapter: AdminComplainAdapter

    private lateinit var binding: ActivityAdminComplaintBoxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_complaint_box)
        binding = ActivityAdminComplaintBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //! arraylist
        complainModalArrayList = ArrayList()

        //! firebase
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("Complains")

        //! Recycler View
        binding.rvAdminComplain.layoutManager = LinearLayoutManager(this)
        adminComplainAdapter = AdminComplainAdapter(complainModalArrayList)
        binding.rvAdminComplain.adapter = adminComplainAdapter

        getAllComplains()
    }

    private fun getAllComplains() {
        complainModalArrayList.clear()
        databaseReference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let { complainModalArrayList.add(it) }
                adminComplainAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let { complainModalArrayList.add(it) }
                adminComplainAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(ComplainModal::class.java)?.let { complainModalArrayList.add(it) }
                adminComplainAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let { complainModalArrayList.add(it) }
                adminComplainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Do nothing
            }

        })
    }
}