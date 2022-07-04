package com.example.bustracking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityComplaintBoxBinding

class ComplaintBox : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintBoxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_box)
        binding = ActivityComplaintBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Complaint Box"

        binding.btnSubmit.setOnClickListener {
            if (binding.etComplainBox.text.isEmpty()) {
                Toast.makeText(this, "Enter Your Complain first then press submit", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.etComplainBox.setText("")
                Toast.makeText(this, "Your Complain has been Submitted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}