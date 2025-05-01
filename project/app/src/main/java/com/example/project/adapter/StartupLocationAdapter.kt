package com.example.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.StartupInfo
import com.example.project.model.TechParkInfo

// Sealed interface/class is good for representing fixed types like this
sealed interface DisplayItem
data class TechParkHeaderItem(val techPark: TechParkInfo) : DisplayItem
data class StartupItem(val startup: StartupInfo) : DisplayItem

class StartupLocationAdapter(
    techParks: List<TechParkInfo>
) : RecyclerView.Adapter<StartupLocationAdapter.LocationViewHolder>() {

    private val displayItems: List<DisplayItem> = flattenData(techParks)

    private fun flattenData(techParks: List<TechParkInfo>): List<DisplayItem> {
        val items = mutableListOf<DisplayItem>()
        techParks.forEach { park ->
            items.add(TechParkHeaderItem(park))
            park.startups.forEach { startup ->
                items.add(StartupItem(startup))
            }
        }
        return items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_startup_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(displayItems[position])
    }

    override fun getItemCount(): Int = displayItems.size

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val techParkNameTextView: TextView = itemView.findViewById(R.id.techParkNameTextView)
        private val techParkContextTextView: TextView = itemView.findViewById(R.id.techParkContextTextView)
        private val startupNameTextView: TextView = itemView.findViewById(R.id.startupNameTextView)
        private val startupDescriptionTextView: TextView = itemView.findViewById(R.id.startupDescriptionTextView)

        fun bind(item: DisplayItem) {
            techParkNameTextView.visibility = View.GONE
            techParkContextTextView.visibility = View.GONE
            startupNameTextView.visibility = View.GONE
            startupDescriptionTextView.visibility = View.GONE

            when (item) {
                is TechParkHeaderItem -> {
                    techParkNameTextView.text = item.techPark.name
                    techParkContextTextView.text = item.techPark.context
                    techParkNameTextView.visibility = View.VISIBLE
                    techParkContextTextView.visibility = View.VISIBLE
                }
                is StartupItem -> {
                    startupNameTextView.text = item.startup.name
                    startupDescriptionTextView.text = item.startup.description
                    startupNameTextView.visibility = View.VISIBLE
                    startupDescriptionTextView.visibility = View.VISIBLE
                }
            }
        }
    }
} 