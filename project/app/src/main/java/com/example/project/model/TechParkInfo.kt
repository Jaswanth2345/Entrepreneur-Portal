package com.example.project.model

data class TechParkInfo(
    val name: String,
    val context: String, // Optional: Tech park description
    val startups: List<StartupInfo>
) 