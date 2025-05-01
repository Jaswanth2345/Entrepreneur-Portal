package com.example.project.model

data class CityInfo(
    val name: String,
    val description: String, // Optional: City description
    val techParks: List<TechParkInfo>
) 