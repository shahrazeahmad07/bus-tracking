package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityStudentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class StudentHome : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.btnSchedule.setOnClickListener {
            startActivity(Intent(this, RouteSchedule::class.java))
        }

        binding.btnChatBox.setOnClickListener {
            startActivity(Intent(this,ChatBox::class.java))
        }

        binding.btnComplainBox.setOnClickListener {
            startActivity(Intent(this, ComplaintBox::class.java))
        }

        supportActionBar?.title = "Student Home"
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