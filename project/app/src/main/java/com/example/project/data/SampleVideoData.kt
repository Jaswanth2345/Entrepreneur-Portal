package com.example.project.data

import com.example.project.R // Import R to access menu item IDs

// Using an 'object' makes these functions easily callable like static methods
object SampleVideoData {

    // --- Sample Data Collections ---

    private fun getSuccessStories(): List<VideoItem> {
        return listOf(
            VideoItem("rlGPg-f3qF4", "The Story of Telegram", "Discover the fascinating journey behind Telegram — from its creation by the Durov brothers to becoming one of the world's most popular messaging platforms."),
            VideoItem("UF8uR6Z6KLc", "Steve Jobs' 2005 Stanford Commencement Address", "A timeless speech on connecting the dots, love and loss, and death."),
            VideoItem("WOoJh6oYAXE", "Bill Gates: The Best Teacher I Never Had", "A tribute to physicist Richard Feynman, highlighting his influence on Gates."),
            VideoItem("mh45igK4Esw", "Elon Musk: How I Became The Real 'Iron Man'", "An in-depth profile of Musk's ventures and visionary approach."),
            VideoItem("5WiDIhIkPoM", "Mark Zuckerberg: Building the Facebook Empire", "An early interview discussing Facebook's growth and vision.")
        )
    }

    private fun getEntrepreneurStories(): List<VideoItem> {
        return listOf(
            VideoItem("tfAhTtBlb2Q", "Jeff Bezos: How to Build an Empire", "Insights into Amazon's founding and growth strategies."),
            VideoItem("S7Q_ytaeUpY", "Jack Ma: Advice for Young People", "Inspirational guidance from Alibaba's founder."),
            VideoItem("2FSE3TNFkJQ", "Larry Page and Sergey Brin: The Genesis of Google", "A look into the early days and vision behind Google."),
            VideoItem("pkAum45ubWc", "Reid Hoffman: Starting a Business", "Advice from LinkedIn's co-founder on entrepreneurship."),
            VideoItem("VBeqIhT941o", "Sara Blakely: Trust Your Gut", "The story of Spanx's founder and her journey to success.")
        )
    }

    private fun getFamousCeos(): List<VideoItem> {
        return listOf(
            VideoItem("pYEAVoGAQq4", "Satya Nadella: Hit Refresh", "Discussing Microsoft's transformation under his leadership."),
            VideoItem("2d77eacLaNo", "Tim Cook: Leading with Values", "Insights into Apple's leadership and values post-Jobs."),
            VideoItem("SccihQgLtHo", "Sundar Pichai: Vision for AI", "Keynote snippets discussing AI advancements at Google."),
            VideoItem("lnEPow0pWAk", "Mary Barra: Leading Change in Automotive", "Discussion on EVs and the future of transportation."),
            VideoItem("ooTX6E_Is7o", "Andy Jassy: Taking the Reins", "Interviews discussing Amazon's future post-Bezos.")
        )
    }

    private fun getTedTalks(): List<VideoItem> {
        return listOf(
            VideoItem("mh45igK4Esw", "Elon Musk: The Future We're Building – and Boring", "TED Talk on SpaceX, Tesla, SolarCity, and The Boring Company."),
            VideoItem("2FSE3TNFkJQ", "Simon Sinek: How Great Leaders Inspire Action", "The Golden Circle concept and leadership insights."),
            VideoItem("6Af6b_wyiwI", "Bill Gates: The Next Outbreak? We're Not Ready", "A prescient talk from 2015 on pandemic preparedness."),
            VideoItem("iG9CE55wbtY", "Sir Ken Robinson: Do Schools Kill Creativity?", "A famous talk on education reform and creativity."),
            VideoItem("86x-u-tz0MA", "Elizabeth Gilbert: Your Elusive Creative Genius", "Perspective on creativity and the pressures of success.")
        )
    }

    private fun getTechMotivation(): List<VideoItem> {
        return listOf(
            VideoItem("DbW088AtTVk", "Arnold Schwarzenegger: 6 Rules of Success", "A classic motivational speech applicable to many fields."),
            VideoItem("s5KmCGmkI1k", "Steve Jobs: One Last Thing (Rare Interview)", "A compilation of insights from the Apple co-founder."),
            VideoItem("BwFOwyoH-3g", "Tony Robbins: Why We Do What We Do", "TED Talk on understanding human motivation."),
            VideoItem("XsVT_crS71E", "From Failure to Success: Jack Ma", "Discussing the benefits of failure and the importance of imagination."),
            VideoItem("sBAqF00gBGk", "Admiral McRaven: Make Your Bed Speech", "A motivational speech on changing the world by starting small.")
        )
    }
    // --- Helper Function to Retrieve Data by Category ID ---

    // Make this function public so MainActivity can call it
    fun getVideosForCategory(categoryId: Int): List<VideoItem> {
        return when (categoryId) {
            R.id.nav_success_stories -> getSuccessStories()
            R.id.nav_entrepreneur_stories -> getEntrepreneurStories()
            R.id.nav_famous_ceos -> getFamousCeos()
            R.id.nav_ted_talks -> getTedTalks()
            R.id.nav_tech_motivation -> getTechMotivation()
            else -> emptyList() // Return empty list for unknown IDs
        }
    }
} 