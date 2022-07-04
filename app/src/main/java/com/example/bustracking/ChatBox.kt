package com.example.bustracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ChatBox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_box)

        supportActionBar?.title = "Chat Box"
    }
}