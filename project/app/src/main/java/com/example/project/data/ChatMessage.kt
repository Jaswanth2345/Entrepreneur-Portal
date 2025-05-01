package com.example.project.data

data class ChatMessage(
    val text: String,
    val isUser: Boolean // True if the message is from the user, false if from the bot
) 