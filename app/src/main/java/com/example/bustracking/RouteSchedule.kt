package com.example.bustracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RouteSchedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_schedule)

        supportActionBar?.title = "Route Schedule"
    }
}