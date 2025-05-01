package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Account"

        FirebaseApp.initializeApp(this)

        emailEditText = findViewById(R.id.registerUsernameEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        signupButton = findViewById(R.id.confirmRegisterButton)

        auth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            handleSignup()
        }
    }

    private fun handleSignup() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
             Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
             return
        }

        signupButton.isEnabled = false
        Toast.makeText(this, "Creating Account...", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                signupButton.isEnabled = true
                if (task.isSuccessful) {
                    Log.d("SignupActivity", "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Signup successful. Please Login.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()

                } else {
                    Log.w("SignupActivity", "createUserWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.message ?: "Signup failed."
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 