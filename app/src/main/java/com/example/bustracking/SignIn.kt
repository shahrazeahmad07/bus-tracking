package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        //! Login Button
        binding.btnLogin.setOnClickListener{
            val internetStatus = checkConnectivity()
            if (!internetStatus) {
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show()
            } else {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Enter All Credentials", Toast.LENGTH_SHORT).show()
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Login Successful!!!", Toast.LENGTH_SHORT).show()
                            //! enabling proper view for proper user
                            val partsOfEmail = email.split('@')
                            //! For Bus Driver
                            if (partsOfEmail[1] == "bus.com") {
                                startActivity(Intent(this, Home::class.java))
                            }
                            //! For admin
                            else if (partsOfEmail[1] == "admin.com") {
                                startActivity(Intent(this, Admin::class.java))
                            }
                            //! for students
                            else {
                                startActivity(Intent(this, StudentHome::class.java))
                            }
                            finish()
                        }
                        else {
                            Toast.makeText(this, "Invalid Credentials...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        //! Sign Up
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }



    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            //! enabling proper view for proper user
            val username = mAuth.currentUser?.email.toString()
            val partsOfEmail = username.split('@')
            //! For Bus Driver
            if (partsOfEmail[1] == "bus.com") {
                startActivity(Intent(this, Home::class.java))
            }
            //! for admin
            else if (partsOfEmail[1] == "admin.com") {
                startActivity(Intent(this, Admin::class.java))
            }
            //! for students.
            else {
                startActivity(Intent(this, StudentHome::class.java))
            }
            finish()
        }
    }

    //! Check Internet Connection
    private fun checkConnectivity(): Boolean {
        return try {
            val command = "ping -c 1 google.com"
            (Runtime.getRuntime().exec(command).waitFor() == 0)
        } catch (e: Exception) {
            false
        }
    }
}