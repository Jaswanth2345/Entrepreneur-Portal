package com.example.project

import android.content.Intent // Needed for potential future navigation
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.VideoListAdapter
import com.example.project.data.VideoItem // Import data class
import com.example.project.data.SampleVideoData // Import the new object
import com.google.android.material.navigation.NavigationView
import android.widget.ImageButton // Import ImageButton
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.project.databinding.ActivityMainBinding // If using ViewBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoListAdapter
    private lateinit var chatButton: ImageButton // Add chat button property
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) // Uses the new layout with DrawerLayout

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler_view_videos)
        chatButton = findViewById(R.id.chat_button) // Find the chat button
//        navigationView.itemIconTintList = null
        // --- Toolbar Setup ---
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance() // Get Firebase Auth instance

        // Check if user is logged in, redirect if not (optional but good practice)
        if (auth.currentUser == null) {
            navigateToLogin()
            return // Prevent rest of MainActivity setup if not logged in
        }

        // --- Navigation Drawer Setup ---
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close // Add these strings to res/values/strings.xml
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // --- Adjust main content padding for edge-to-edge ---
        val contentContainer = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_content_container) // Use the correct container ID
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                androidx.core.graphics.Insets.of(0, 0, 0, 0)
            ).build()
        }

        // --- RecyclerView Setup ---
        setupRecyclerView()

        // --- Chat Button Listener ---
        chatButton.setOnClickListener {
            showChatDialog()
        }

        // --- Load Initial Category (Optional) ---
        if (savedInstanceState == null) {
            val defaultCategoryId = R.id.nav_success_stories
            navigationView.setCheckedItem(defaultCategoryId)
            loadVideos(defaultCategoryId)
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoListAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter
    }

    private fun loadVideos(categoryId: Int) {
        val videos = SampleVideoData.getVideosForCategory(categoryId)
        videoAdapter.updateData(videos)
        // Optional: Update Toolbar title
        title = navigationView.menu.findItem(categoryId)?.title
    }

    // Handle clicks on navigation items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Handle video categories
            R.id.nav_success_stories,
            R.id.nav_entrepreneur_stories,
            R.id.nav_famous_ceos,
            R.id.nav_ted_talks,
            R.id.nav_tech_motivation -> {
                loadVideos(item.itemId)
            }

            // Handle Roadmap Generator click
            R.id.nav_roadmap_generator -> {
                val intent = Intent(this, RoadmapActivity::class.java)
                startActivity(intent)
                 item.isChecked = false
            }

            // *** Add Logout Case ***
            R.id.nav_logout -> {
                performLogout()
            }

            // *** Add Locate Startups Case ***
            R.id.nav_locate_startups -> {
                val intent = Intent(this, LocateStartupsActivity::class.java)
                startActivity(intent)
            }

            else -> return false // Unknown item
        }

        drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer
        return true // Indicate item selection was handled
    }

    // Handle back press to close drawer if open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed() // Default back behavior
        }
    }

    private fun showChatDialog() {
        val chatDialog = ChatDialogFragment()
        chatDialog.show(supportFragmentManager, "ChatDialogFragment")
    }

    // *** Function to handle logout logic ***
    private fun performLogout() {
        auth.signOut() // Sign out from Firebase
        navigateToLogin()
    }

    // *** Function to navigate back to Login ***
    private fun navigateToLogin() {
         val intent = Intent(this, LoginActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         startActivity(intent)
         finish() // Finish MainActivity
    }

    // (Need to add string resources navigation_drawer_open and navigation_drawer_close in res/values/strings.xml)
}