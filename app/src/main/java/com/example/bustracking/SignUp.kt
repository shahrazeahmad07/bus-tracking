package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        //! Sign In Instead
        binding.tvLoginInstead.setOnClickListener {
            finish()
        }

        //! Sign Up Button
        binding.btnSignUp.setOnClickListener {
            val internetStatus = checkConnectivity()
            if (!internetStatus) {
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show()
            } else {
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Enter All Credentials", Toast.LENGTH_SHORT).show()
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Sign up Successful..!", Toast.LENGTH_SHORT).show()

                            //! enabling proper view for proper user
                            val partsOfEmail = email.split('@')
                            //! For Bus Driver
                            if (partsOfEmail[1] == "bus.com") {
                                startActivity(Intent(this, Home::class.java))
                            }
                            //! for Admin
                            else if (partsOfEmail[1] == "admin.com") {
                                startActivity(Intent(this, Admin::class.java))
                            }
                            //! for students.
                            else {
                                startActivity(Intent(this, StudentHome::class.java))
                            }
                            finish()
                        }
                        else {
                            val errorMessage = it.exception?.message.toString()
                            Toast.makeText(this, "$errorMessage ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
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