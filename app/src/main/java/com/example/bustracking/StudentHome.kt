package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.databinding.ActivityStudentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentHome : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var mAuth: FirebaseAuth

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var rvBusDriverModalArrayList: ArrayList<RVBusDriverModal>
    private lateinit var rvAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Student Home"
        mAuth = FirebaseAuth.getInstance()

        rvBusDriverModalArrayList = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Locations")

        binding.rvBusDrivers.layoutManager = LinearLayoutManager(this)
        rvAdapter = RecyclerAdapter(rvBusDriverModalArrayList)
        binding.rvBusDrivers.adapter = rvAdapter
        getAllBusDrivers()

        binding.btnSchedule.setOnClickListener {
            startActivity(Intent(this, RouteSchedule::class.java))
        }

        binding.btnChatBox.setOnClickListener {
            startActivity(Intent(this, ChatBox::class.java))
        }

        binding.btnComplainBox.setOnClickListener {
            startActivity(Intent(this, ComplaintBox::class.java))
        }

        binding.tvBus.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
    private fun getAllBusDrivers() {
        rvBusDriverModalArrayList.clear()
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
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