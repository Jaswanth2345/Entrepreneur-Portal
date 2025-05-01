package com.example.project

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText // Keep EditText for finding view
import android.widget.ImageButton
import android.widget.ProgressBar // Import ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.ChatAdapter
import com.example.project.data.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.android.material.textfield.TextInputEditText // Use TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.ai.client.generativeai.type.Content // Import Content for system instructions
// import com.google.ai.client.generativeai.type.defineContent // Import helper function

class ChatDialogFragment : DialogFragment() {

    // Remove old TextView
    // private lateinit var chatOutput: TextView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatInput: TextInputEditText // Use TextInputEditText
    private lateinit var sendButton: ImageButton
    private lateinit var loadingIndicator: ProgressBar // Add ProgressBar
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>() // Store messages here
    private var generativeModel: GenerativeModel? = null

    // Use onCreateView for custom layout instead of onCreateDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the new modern layout
        return inflater.inflate(R.layout.dialog_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views using new IDs
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view)
        chatInput = view.findViewById(R.id.chat_input_modern) // Use new input ID
        sendButton = view.findViewById(R.id.send_button_modern) // Use new button ID
        loadingIndicator = view.findViewById(R.id.loading_indicator) // Find progress bar

        setupRecyclerView()
        setupGeminiModel() // Initialize the model

        sendButton.setOnClickListener {
            sendMessage()
        }

        // Add initial bot message if list is empty
        if (messageList.isEmpty()) {
             addMessageToList("Hello! How can I help you today?", false)
        }
    }

    // Optional: Adjust dialog size if needed
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) // Adjust size
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messageList)
        val layoutManager = LinearLayoutManager(requireContext())
        // layoutManager.stackFromEnd = true // Messages appear from bottom (optional)
        chatRecyclerView.layoutManager = layoutManager
        chatRecyclerView.adapter = chatAdapter
    }


    private fun setupGeminiModel() {
        val apiKey = BuildConfig.GEMINI_API_KEY
        Log.d("ChatDialogFragment", "API Key from BuildConfig: '$apiKey'")
        if (apiKey.isBlank()) {
            Log.e("ChatDialogFragment", "API Key is BLANK.")
            addMessageToList("Error: Chatbot not configured. Missing API Key.", false)
            sendButton.isEnabled = false
            chatInput.isEnabled = false
            return
        }
        try {
            // Define the system instruction text (same as before)
            val systemInstructionText = """
                You are a knowledgeable and helpful assistant specializing in the fields of technology, 
                entrepreneurship, and startups. Your goal is to provide insightful advice, 
                relevant information, definitions, and analysis related to these domains. 
                When asked general questions, try to frame your answers through the lens of tech 
                or business if appropriate, but remain helpful for all queries.
                """.trimIndent()

            // *** Try passing the String directly ***
            // We create a simple Content object containing the text, without using defineContent()
            // Or check if the constructor takes a String directly for systemInstruction.
            // The most likely parameter type is still Content, so let's create it manually.
             val instruction = Content(role = "system", parts = listOf(com.google.ai.client.generativeai.type.TextPart(systemInstructionText)))
            // If the above line gives errors about Content constructor or TextPart,
            // then the API *might* accept a raw String, but it's less common.


            generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey,
                // Pass the manually created Content object or potentially the raw string
                systemInstruction = instruction
                // Alternative if Content(...) constructor fails:
                // systemInstruction = systemInstructionText // Less likely to work, but worth a try if Content fails
            )
            Log.i("ChatDialogFragment", "Gemini model initialized with system instruction.")
            sendButton.isEnabled = true
            chatInput.isEnabled = true
        } catch (e: Exception) {
             showError("Failed to initialize Gemini model: ${e.localizedMessage}")
             addMessageToList("Error: Chatbot failed to initialize.", false)
             generativeModel = null
             sendButton.isEnabled = false
             chatInput.isEnabled = false
             Log.e("ChatDialogFragment", "Model initialization failed", e)
        }
    }

    private fun sendMessage() {
        val inputText = chatInput.text.toString().trim()
        if (inputText.isEmpty()) {
            showError("Please enter a message.")
            return
        }
        val model = generativeModel
        if (model == null) {
            showError("Chat model is not initialized.")
            setupGeminiModel()
            return
        }

        // Add user message to RecyclerView
        addMessageToList(inputText, true)
        chatInput.text?.clear() // Clear input field

        showLoading(true) // Show progress bar, disable input

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response: GenerateContentResponse = model.generateContent(inputText)
                withContext(Dispatchers.Main) {
                    val responseText = response.text
                    if (responseText != null) {
                        // Add bot response to RecyclerView
                        addMessageToList(responseText, false)
                    } else {
                        addMessageToList("Gemini: Received no text content.", false)
                        showError("Gemini response was empty or blocked.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("API Error: ${e.localizedMessage}")
                    addMessageToList("Error: Failed to get response from Gemini.", false)
                    Log.e("ChatDialogFragment", "API call failed", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    showLoading(false) // Hide progress bar, enable input
                }
            }
        }
    }

    // Helper to add message and scroll
    private fun addMessageToList(text: String, isUser: Boolean) {
        messageList.add(ChatMessage(text, isUser))
        chatAdapter.notifyItemInserted(messageList.size - 1)
        // Scroll to the bottom to show the latest message
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingIndicator.visibility = View.VISIBLE
            sendButton.isEnabled = false
            chatInput.isEnabled = false
        } else {
            loadingIndicator.visibility = View.GONE
            sendButton.isEnabled = true
            chatInput.isEnabled = true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    // Removed onCreateDialog as we are using onCreateView now
    // override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { ... }
}