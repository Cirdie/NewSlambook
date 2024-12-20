package com.example.slmabookfinal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.slmabookfinal.databinding.ActivitySlambookHomeBinding

class SlambookHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlambookHomeBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySlambookHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ViewModel
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        val selectedSlambook = intent.getSerializableExtra("selectedSlambook") as? SlambookEntry

        selectedSlambook?.let {
            sharedViewModel.setSlambookData(it)
        }
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}
