package com.example.slmabookfinal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.slmabookfinal.databinding.ActivitySlambookHomeBinding

class SlambookHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlambookHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivitySlambookHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge layout for a better user experience
        enableEdgeToEdge()

        // Apply edge-to-edge layout adjustments to ensure no content is hidden by system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up BottomNavigationView to work with NavController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }

    override fun onBackPressed() {
        // Handle the back button to control navigation stack properly
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (!navController.popBackStack()) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }
}
