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

class LoginActivity : AppCompatActivity() {

    // Assuming usernameEditText is for email input
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        // --- Check if user is already signed in ---
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, go directly to MainActivity
            Log.d("LoginActivity", "User already logged in: ${currentUser.uid}")
            navigateToMain()
            // Return here to prevent setting the login layout if already logged in
            return
        }
        // --- End Check ---

        // If user is not logged in, set the content view for login
        setContentView(R.layout.activity_login)

        // Find views by their IDs
        emailEditText = findViewById(R.id.usernameEditText) // Treat as email
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        // Set button listeners
        loginButton.setOnClickListener {
            handleLogin()
        }

        registerButton.setOnClickListener {
            handleSignupNavigation()
        }
    }

    private fun handleLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // --- Input Validation ---
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            return
        }
        // --- End Validation ---

        // Disable button while processing
        loginButton.isEnabled = false
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()

        // --- Firebase Login Logic ---
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                 loginButton.isEnabled = true // Re-enable button when done
                if (task.isSuccessful) {
                    // Sign in success, navigate to the main activity
                    Log.d("LoginActivity", "signInWithEmail:success")
                    navigateToMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.message ?: "Authentication failed."
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        // --- End Firebase Logic ---
    }

     // Navigate to MainActivity after successful login
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        // Clear back stack so user can't go back to login screen
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finish LoginActivity
    }

    // Navigate to SignupActivity
    private fun handleSignupNavigation() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
} 