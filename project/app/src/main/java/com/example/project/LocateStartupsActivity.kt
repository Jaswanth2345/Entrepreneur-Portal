package com.example.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.CityAdapter
import com.example.project.adapter.StartupLocationAdapter
import com.example.project.model.CityInfo
import com.example.project.model.StartupInfo
import com.example.project.model.TechParkInfo
import com.example.project.databinding.ActivityLocateStartupsBinding

import com.example.project.R

class LocateStartupsActivity : AppCompatActivity(), CityAdapter.OnCityClickListener {

    private lateinit var binding: ActivityLocateStartupsBinding
    private lateinit var citiesRecyclerView: RecyclerView
    private lateinit var startupsRecyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private var allCitiesData: List<CityInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocateStartupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        citiesRecyclerView = binding.citiesRecyclerView
        startupsRecyclerView = binding.startupsRecyclerView

        citiesRecyclerView.layoutManager = LinearLayoutManager(this)
        startupsRecyclerView.layoutManager = LinearLayoutManager(this)

        loadStartupData()
        setupCityList()
    }

    private fun setupCityList() {
        title = "Startup Locations"
        binding.citiesRecyclerView.adapter = CityAdapter(allCitiesData, this)

        binding.citiesRecyclerView.visibility = View.VISIBLE
        binding.startupsRecyclerView.visibility = View.GONE
    }

    private fun showStartupDetails(city: CityInfo) {
        title = city.name
        binding.startupsRecyclerView.adapter = StartupLocationAdapter(city.techParks)

        binding.citiesRecyclerView.visibility = View.GONE
        binding.startupsRecyclerView.visibility = View.VISIBLE
    }

    override fun onCityClick(city: CityInfo) {
        showStartupDetails(city)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.startupsRecyclerView.visibility == View.VISIBLE) {
            setupCityList()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadStartupData() {
        val bengaluruParks = listOf(
            TechParkInfo("International Tech Park Bangalore (ITPB), Whitefield",
                "Established in 1994, hosts global giants and supports startups.",
                listOf(
                    StartupInfo("Sarvam AI", "Developing voice-based large language models for India."),
                    StartupInfo("Supersourcing", "AI-powered recruitment platform for engineers.")
                )),
            TechParkInfo("Manyata Tech Park, Outer Ring Road",
                "Known for modern infrastructure and connectivity.",
                listOf(
                    StartupInfo("Swiggy", "Leading food delivery platform."),
                    StartupInfo("BharatPe", "Fintech startup for small business digital payments.")
                )),
            TechParkInfo("Electronics City",
                "Bengaluru's largest tech hub, hosting startups and giants like Infosys.",
                listOf(
                    StartupInfo("Byju's", "Global edtech leader."),
                    StartupInfo("CleverTap", "Mobile marketing and customer engagement platform.")
                )),
            TechParkInfo("Bagmane Tech Park",
                "Strategically located near Indiranagar with integrated amenities.",
                listOf(
                    StartupInfo("Dunzo", "Hyper-local delivery app."),
                    StartupInfo("Slice", "Fintech startup for credit and payment solutions.")
                ))
        )
        val puneParks = listOf(
            TechParkInfo("Rajiv Gandhi Infotech Park, Hinjawadi",
                "Pune's largest IT park with incubation centers.",
                listOf(
                    StartupInfo("NeoGrowth", "Digital lending platform for SMBs."),
                    StartupInfo("ZestMoney", "Fintech company specializing in consumer lending.")
                )),
            TechParkInfo("IndiaLand Global Tech Park",
                "Emphasizes innovation with incubation facilities.",
                listOf(
                    StartupInfo("Unocoin", "Cryptocurrency service provider.")
                ))
        )
        val hyderabadParks = listOf(
            TechParkInfo("HITEC City",
                "Major IT hub fostering startup growth.",
                listOf(
                    StartupInfo("HealthifyMe", "Healthtech startup for fitness and nutrition."),
                    StartupInfo("Cashfree", "Payments and banking technology startup.")
                )),
            TechParkInfo("T-Hub",
                "India's largest startup incubator.",
                listOf(
                    StartupInfo("Hasura", "Software development tools company.")
                ))
        )
        val chennaiParks = listOf(
            TechParkInfo("Tidel Park",
                "Key IT hub with facilities supporting startups.",
                listOf(
                    StartupInfo("Freshworks", "Cloud-based CRM software company."),
                    StartupInfo("Get My Parking", "B2B platform-as-a-service for parking.")
                ))
        )
        val delhiNcrParks = listOf(
            TechParkInfo("Noida Special Economic Zone (NSEZ)",
                "Major IT park in Noida attracting startups.",
                listOf(
                    StartupInfo("Paytm", "Leading digital payments platform."),
                    StartupInfo("Tata 1mg", "Healthtech unicorn (online pharmacy/diagnostics).")
                )),
            TechParkInfo("Cyber City, Gurugram",
                "Premium tech hub with co-working spaces.",
                listOf(
                    StartupInfo("Zomato", "Food delivery and restaurant discovery platform."),
                    StartupInfo("InMobi", "Mobile marketing platform.")
                ))
        )
        val mumbaiParks = listOf(
            TechParkInfo("Mindspace, Malad",
                "Offers connectivity and community setting.",
                listOf(
                    StartupInfo("Games24x7", "Online gaming platform (RummyCircle, My11Circle)."),
                    StartupInfo("Nykaa", "E-commerce platform for beauty and wellness.")
                )),
            TechParkInfo("Bandra Kurla Complex (BKC)",
                "Premier business district with modern spaces.",
                listOf(
                    StartupInfo("Zepto", "Quick-commerce grocery delivery.")
                ))
        )

        allCitiesData = listOf(
            CityInfo("Bengaluru", "Silicon Valley of India", bengaluruParks),
            CityInfo("Pune", "Growing tech hub", puneParks),
            CityInfo("Hyderabad", "Hub for fintech, healthtech, AI", hyderabadParks),
            CityInfo("Chennai", "Supports edtech, fintech, SaaS", chennaiParks),
            CityInfo("Delhi-NCR", "Hosts e-commerce, healthtech, fintech", delhiNcrParks),
            CityInfo("Mumbai", "Supports gaming, e-commerce, fintech", mumbaiParks)
        )
    }
} 