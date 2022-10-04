package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.RecyclerAdapter
import com.example.bustracking.databinding.ActivityAdminBinding
import com.example.bustracking.modals.RVBusDriverModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Admin : AppCompatActivity(), RecyclerAdapter.RecyclerAdapterInterface{
    private lateinit var binding: ActivityAdminBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var rvBusDriverModalArrayList: ArrayList<RVBusDriverModal>
    private lateinit var rvAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //! array initialization
        rvBusDriverModalArrayList = ArrayList()

        //! firebase initializations.
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Locations")

        //! recycler View for bus drivers
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        rvAdapter = RecyclerAdapter(rvBusDriverModalArrayList, this)
        binding.recyclerView.adapter = rvAdapter

        //! getting all bus drivers
        getAllBusDrivers()

        //! button update schedule
        binding.btnUpdateSchedule.setOnClickListener {
            startActivity(Intent(this, UpdateSchedule::class.java))
        }

        //! button show complaint box
        binding.btnShowComplaintBox.setOnClickListener {
            startActivity(Intent(this, AdminComplaintBox::class.java))
        }
    }

    private fun getAllBusDrivers() {
        rvBusDriverModalArrayList.clear()
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                binding.progressBar.visibility = View.GONE
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                binding.progressBar.visibility = View.GONE
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                binding.progressBar.visibility = View.GONE
                rvAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RVBusDriverModal::class.java)
                    ?.let { rvBusDriverModalArrayList.add(it) }
                binding.progressBar.visibility = View.GONE
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

    override fun onBusClick(position: Int) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("busName", rvBusDriverModalArrayList[position].userName)
        startActivity(intent)
    }
}