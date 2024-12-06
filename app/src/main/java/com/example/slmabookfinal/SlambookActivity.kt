package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slmabookfinal.databinding.ActivitySlambookBinding

class SlambookActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlambookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlambookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get list of slambooks from repository
        val slambooks = SlambookRepository.getSlambooks()

        // If slambooks are not empty, set up RecyclerView
        if (slambooks.isNotEmpty()) {
            setupRecyclerView(slambooks)
        }

        // Set up click listener for creating new slambook
        binding.createSlambookButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
    }

    private fun setupRecyclerView(slambooks: List<SlambookEntry>) {
        // Set up RecyclerView with LinearLayoutManager and adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = SlambookListAdapter(slambooks) { selectedSlambook ->
            // When a slambook is selected, pass the data to SlambookHomeActivity
            openSlambookHomeActivity(selectedSlambook)
        }
    }

    private fun openSlambookHomeActivity(slambook: SlambookEntry) {
        // Create an Intent to start SlambookHomeActivity
        val intent = Intent(this, SlambookHomeActivity::class.java).apply {
            // Pass the selected slambook as an extra
            putExtra("selectedSlambook", slambook)
        }
        startActivity(intent)
    }
}
