package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slmabookfinal.databinding.ActivitySlambookBinding

class SlambookActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlambookBinding
    private lateinit var adapter: SlambookListAdapter
    private val slambooks = mutableListOf<SlambookEntry>() // MutableList for dynamic updates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlambookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load slambooks from repository
        slambooks.addAll(SlambookRepository.getSlambooks())

        setupRecyclerView()

        // Set up click listener for creating new slambook
        binding.createSlambookButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        // Update UI based on whether there are slambooks
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        // Initialize adapter with click and remove callbacks
        adapter = SlambookListAdapter(
            slambooks = slambooks,
            onItemClick = { selectedSlambook ->
                openSlambookHomeActivity(selectedSlambook)
            },
            onRemoveClick = { slambookToRemove ->
                deleteSlambook(slambookToRemove)
            }
        )

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun openSlambookHomeActivity(slambook: SlambookEntry) {
        // Start SlambookHomeActivity with selected slambook
        val intent = Intent(this, SlambookHomeActivity::class.java).apply {
            putExtra("selectedSlambook", slambook)
        }
        startActivity(intent)
    }

    private fun deleteSlambook(slambook: SlambookEntry) {
        // Remove from repository
        SlambookRepository.deleteSlambook(slambook)

        // Remove from adapter and update UI
        adapter.removeSlambook(slambook)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        // Show or hide empty state illustration and text
        if (slambooks.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
}
