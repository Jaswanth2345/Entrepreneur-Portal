 package com.example.project.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.ChatMessage

class ChatAdapter(private val messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chat_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        // Notify that an item was inserted at the last position
        notifyItemInserted(messages.size - 1)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val messageContainer: LinearLayout = itemView as LinearLayout // Get the root LinearLayout

        fun bind(message: ChatMessage) {
            messageText.text = message.text

            if (message.isUser) {
                // User message: Align right, use user bubble background, white text
                messageContainer.gravity = Gravity.END
                messageText.background = ContextCompat.getDrawable(itemView.context, R.drawable.chat_bubble_user)
                messageText.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            } else {
                // Bot message: Align left, use bot bubble background, default text color
                messageContainer.gravity = Gravity.START
                messageText.background = ContextCompat.getDrawable(itemView.context, R.drawable.chat_bubble_bot)
                 // Use primary text color from theme (or set explicitly)
                messageText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black // Make sure textColorPrimary is defined in colors.xml or use android default
                    // Or use: ContextCompat.getColor(itemView.context, android.R.color.black)
                ))
            }
        }
    }
}