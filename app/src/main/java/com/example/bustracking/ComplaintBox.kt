package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.StudentComplainRecyclerAdapter
import com.example.bustracking.databinding.ActivityComplaintBoxBinding
import com.example.bustracking.modals.ComplainModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ComplaintBox : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintBoxBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var complainModalArrayList: ArrayList<ComplainModal>

    private lateinit var studentComplainRecyclerAdapter: StudentComplainRecyclerAdapter

    private lateinit var email: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_box)
        binding = ActivityComplaintBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Complaint Box"

        //! btn Write Complain.
        binding.btnWriteComplain.setOnClickListener {
            startActivity(Intent(this, WriteComplain::class.java))
        }

        //! initializing Array
        complainModalArrayList = ArrayList()

        //! firebase initializations
        firebaseAuth = FirebaseAuth.getInstance()
        email = firebaseAuth.currentUser?.email.toString()
        username = email.split("@")[0]
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Complains")

        //! recyclerView
        binding.rvComplains.layoutManager = LinearLayoutManager(this)
        studentComplainRecyclerAdapter = StudentComplainRecyclerAdapter(complainModalArrayList)
        binding.rvComplains.adapter = studentComplainRecyclerAdapter

        //! getting
        getAllComplains()
    }

    private fun getAllComplains() {
        complainModalArrayList.clear()
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let {
                    if (it.author == username) {
                        complainModalArrayList.add(it)
                        studentComplainRecyclerAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let {
                    if (it.author == username) {
                        complainModalArrayList.add(it)
                        studentComplainRecyclerAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(ComplainModal::class.java)?.let {
                    if (it.author == username) {
                        complainModalArrayList.add(it)
                        studentComplainRecyclerAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ComplainModal::class.java)?.let {
                    if (it.author == username) {
                        complainModalArrayList.add(it)
                        studentComplainRecyclerAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Do nothing
            }
        })
    }
}