package com.example.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner // Import LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.VideoItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// Remove the click listener interface, as clicks are handled by the embedded player's play button
// interface OnVideoItemClickListener { ... }

class VideoListAdapter(
    private var videos: List<VideoItem>,
    private val lifecycleOwner: LifecycleOwner // Pass LifecycleOwner from Activity/Fragment
) : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    // No need for initializedPlayers set with the new approach
    // private val initializedPlayers = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_list_item, parent, false) // Use updated layout
        return VideoViewHolder(view, lifecycleOwner) // Pass lifecycle owner
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video) // Pass only the video item
    }

    override fun getItemCount(): Int = videos.size

    fun updateData(newVideos: List<VideoItem>) {
        videos = newVideos
        // No need to clear initializedPlayers
        notifyDataSetChanged() // Consider DiffUtil later
    }

    // ViewHolder handles initializing and binding the player
    class VideoViewHolder(
        itemView: View,
        lifecycleOwner: LifecycleOwner // Store LifecycleOwner from parameter
    ) : RecyclerView.ViewHolder(itemView) {

        // Find views using the new IDs from the layout
        private val titleTextView: TextView = itemView.findViewById(R.id.videoTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.videoDescriptionTextView)
        private val youtubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtubePlayerView)

        private var currentVideoId: String? = null // Keep track of the bound video ID
        private var youTubePlayer: YouTubePlayer? = null // Reference to the player instance

        // Initialize lifecycle observation and player listener once
        init {
            // Add player view to lifecycle observer
            lifecycleOwner.lifecycle.addObserver(youtubePlayerView)

            // Add the listener *once* when the ViewHolder is created
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    // Store the player instance
                    youTubePlayer = player
                    // If a video ID was already set by bind(), load it now
                    currentVideoId?.let {
                        youTubePlayer?.cueVideo(it, 0f)
                    }
                }
            })
        }


        fun bind(videoItem: VideoItem) {
            // Update descriptive views
            titleTextView.text = videoItem.title
            descriptionTextView.text = videoItem.description

            // Store the new video ID. Crucially, do this BEFORE trying to load it.
            currentVideoId = videoItem.id // Assuming 'id' is the YouTube video ID property in VideoItem

            // If the player is ready (not null), cue the NEW video ID.
            // If it's not ready yet, the onReady listener will handle it using the updated currentVideoId.
            youTubePlayer?.cueVideo(currentVideoId!!, 0f) // Use !! as currentVideoId is set just above

        }
    }

    // Optional: Helps release resources when view is recycled, though lifecycle observer should handle most
    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        // Consider explicitly pausing or stopping if needed, but lifecycle observer is preferred
        // holder.youTubePlayer?.pause() // Example
    }
} 