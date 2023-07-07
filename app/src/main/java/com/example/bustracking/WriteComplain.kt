package com.example.bustracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bustracking.databinding.ActivityWriteComplainBinding
import com.example.bustracking.modals.ComplainModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WriteComplain : AppCompatActivity() {
    private lateinit var binding: ActivityWriteComplainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_complain)

        binding = ActivityWriteComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Write Complain"

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("Complains")



        binding.btnSubmit.setOnClickListener {
            if (binding.etComplainBox.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter Your Complain first then press submit",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val etComplain = binding.etComplainBox.text.toString()
                val email = firebaseAuth.currentUser?.email.toString()
                val partsOfEmail = email.split("@")
                val complain = ComplainModal(partsOfEmail[0], etComplain)
//                val hashMap : HashMap<String, String> = HashMap()
//                hashMap["username"] = partsOfEmail[0]
//                hashMap["complain"] = etComplain

                databaseReference.push().setValue(complain)
//                binding.etComplainBox.setText("")
                Toast.makeText(this, "Your Complain has been Submitted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}