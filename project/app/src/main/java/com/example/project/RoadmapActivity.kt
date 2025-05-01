package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoadmapActivity : AppCompatActivity() {

    private lateinit var ideaInput: EditText
    private lateinit var fundingInput: EditText
    private lateinit var generateButton: Button
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var roadmapOutput: TextView
    private lateinit var disclaimerText: TextView // Added for clarity

    private var generativeModel: GenerativeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap)

        // Optional: Add Toolbar and Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Roadmap Generator"

        ideaInput = findViewById(R.id.idea_input)
        fundingInput = findViewById(R.id.funding_input)
        generateButton = findViewById(R.id.generate_button)
        loadingIndicator = findViewById(R.id.roadmap_loading)
        roadmapOutput = findViewById(R.id.roadmap_output)
        disclaimerText = findViewById(R.id.roadmap_disclaimer)

        // Set disclaimer text
        disclaimerText.text = "Disclaimer: Generated roadmaps are illustrative examples based on general patterns and are NOT financial or business advice. Actual needs vary greatly."

        setupGeminiModel() // Initialize Gemini

        generateButton.setOnClickListener {
            val idea = ideaInput.text.toString().trim()
            val fundingString = fundingInput.text.toString().trim()

            if (idea.isEmpty()) {
                showError("Please enter your startup idea.")
                return@setOnClickListener
            }

            val funding = fundingString.toDoubleOrNull()
            if (funding == null || funding <= 0) {
                showError("Please enter a valid positive funding amount.")
                return@setOnClickListener
            }

            generateRoadmap(idea, funding)
        }
    }

     // --- Gemini Initialization (Adapt from ChatDialogFragment) ---
    private fun setupGeminiModel() {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            showError("API Key Error: Cannot initialize generator.")
            generateButton.isEnabled = false // Disable generation
            return
        }
        try {
            generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash", // Or appropriate model
                apiKey = apiKey
                // No system instruction needed here, or a different one
            )
             generateButton.isEnabled = true
        } catch (e: Exception) {
             showError("Initialization Error: ${e.localizedMessage}")
             generateButton.isEnabled = false
        }
    }

    // --- Roadmap Generation ---
    private fun generateRoadmap(idea: String, funding: Double) {
        // ... (Prompt construction and API call as shown above) ...
        val prompt = """
            Generate a very basic, illustrative startup roadmap for the following concept:
            Startup Idea: "$idea"
            Initial Funding: $${funding}

            Outline 3-5 key phases (e.g., Concept/Validation, MVP Development, Initial Launch, Early Traction).
            For each phase:
            1. Briefly describe the main goals or activities.
            2. Provide a *very rough estimated percentage* of the initial funding that *might* be allocated to activities in that phase. Emphasize these are highly variable illustrative examples.

            Format the output clearly.

            Include a disclaimer at the end stating: "Disclaimer: This is a generalized illustration, not financial or business advice. Actual startup costs, timelines, and funding allocations vary significantly based on specific circumstances, market conditions, and execution."
            """.trimIndent()

            showLoading(true)
            roadmapOutput.text = ""

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = generativeModel?.generateContent(prompt)
                    withContext(Dispatchers.Main) {
                        roadmapOutput.text = response?.text ?: "Error: Could not generate roadmap."
                        if (response?.text == null) {
                             showError("Failed to get response from Gemini.")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showError("API Error: ${e.localizedMessage}")
                        roadmapOutput.text = "Error generating roadmap. Please try again."
                        Log.e("RoadmapActivity", "API call failed", e)
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        showLoading(false)
                    }
                }
            }
    }


    // --- Helper Functions ---
    private fun showLoading(isLoading: Boolean) {
        loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        generateButton.isEnabled = !isLoading
        ideaInput.isEnabled = !isLoading
        fundingInput.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

     // Optional: Handle Up button
     override fun onSupportNavigateUp(): Boolean {
         onBackPressedDispatcher.onBackPressed()
         return true
     }
}
