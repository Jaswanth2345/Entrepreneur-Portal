package com.example.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.CityInfo

class CityAdapter(
    private val cities: List<CityInfo>,
    private val listener: OnCityClickListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    interface OnCityClickListener {
        fun onCityClick(city: CityInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city, listener)
    }

    override fun getItemCount(): Int = cities.size

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.cityDescriptionTextView)

        fun bind(city: CityInfo, listener: OnCityClickListener) {
            nameTextView.text = city.name
            descriptionTextView.text = city.description
            itemView.setOnClickListener { listener.onCityClick(city) }
        }
    }
} 