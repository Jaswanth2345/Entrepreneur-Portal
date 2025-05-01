package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

// Suppress Lint warning for Handler Leak, as this is a short-lived activity
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2500 // Delay in milliseconds (e.g., 2.5 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Use a Handler to delay the start of MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an Intent to start MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finish SplashActivity so the user can't navigate back to it
            finish()
        }, SPLASH_DELAY)
    }
} 