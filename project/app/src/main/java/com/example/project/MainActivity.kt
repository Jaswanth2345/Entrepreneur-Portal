package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
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
import com.example.project.data.SampleVideoData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerViewVideos: RecyclerView
    private lateinit var videoAdapter: VideoListAdapter
    private lateinit var chatButton: ImageButton
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        recyclerViewVideos = findViewById(R.id.recycler_view_videos)
        chatButton = findViewById(R.id.chat_button)

        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }

        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val contentContainer = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_content_container)
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                androidx.core.graphics.Insets.of(0, 0, 0, 0)
            ).build()
        }

        setupRecyclerView()

        chatButton.setOnClickListener {
            showChatDialog()

            val postRequest = PostRequest()
            postRequest.sendGeminiRequest("Explain how AI works") { response ->
                runOnUiThread {
                    if (response != null) {
                        Log.d("GeminiResponse", response)
                    } else {
                        Log.e("GeminiError", "Failed to get response")
                    }
                }
            }
        }

        if (savedInstanceState == null) {
            val defaultCategoryId = R.id.nav_success_stories
            navigationView.setCheckedItem(defaultCategoryId)
            loadVideos(defaultCategoryId)
            showVideoContent()
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoListAdapter(emptyList(), this)
        recyclerViewVideos.layoutManager = LinearLayoutManager(this)
        recyclerViewVideos.adapter = videoAdapter
    }

    private fun loadVideos(categoryId: Int) {
        val videos = SampleVideoData.getVideosForCategory(categoryId)
        videoAdapter.updateData(videos)
        title = navigationView.menu.findItem(categoryId)?.title
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_success_stories,
            R.id.nav_entrepreneur_stories,
            R.id.nav_famous_ceos,
            R.id.nav_ted_talks,
            R.id.nav_tech_motivation -> {
                showVideoContent()
                loadVideos(item.itemId)
            }

            R.id.nav_roadmap_generator -> {
                startActivity(Intent(this, RoadmapActivity::class.java))
                item.isChecked = false
            }

            R.id.nav_locate_startups -> {
                startActivity(Intent(this, LocateStartupsActivity::class.java))
            }

            R.id.nav_logout -> {
                performLogout()
            }

            R.id.nav_gemini -> {
                showGeminiFragment()
            }

            else -> return false
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun showChatDialog() {
        val chatDialog = ChatDialogFragment()
        chatDialog.show(supportFragmentManager, "ChatDialogFragment")
    }

    private fun performLogout() {
        auth.signOut()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // --- Added helper functions to switch UI ---

    private fun showGeminiFragment() {
        recyclerViewVideos.visibility = View.GONE
        chatButton.visibility = View.GONE
        findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GeminiFragment())
            .commit()
    }

    private fun showVideoContent() {
        recyclerViewVideos.visibility = View.VISIBLE
        chatButton.visibility = View.VISIBLE
        findViewById<View>(R.id.fragment_container).visibility = View.GONE
    }
}
