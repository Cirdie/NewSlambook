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
    private val slambooks = mutableListOf<SlambookEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlambookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        slambooks.addAll(SlambookStorage.getSlambooks())

        setupRecyclerView()

        binding.createSlambookButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        updateEmptyState()
    }

    private fun setupRecyclerView() {
        adapter = SlambookListAdapter(
            slambooks = slambooks,
            onItemClick = { selectedSlambook ->
                openSlambookHomeActivity(selectedSlambook)
            },
            onRemoveClick = { slambookToRemove ->
                deleteSlambook(slambookToRemove)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun openSlambookHomeActivity(slambook: SlambookEntry) {
        val intent = Intent(this, SlambookHomeActivity::class.java).apply {
            putExtra("selectedSlambook", slambook)
        }
        startActivity(intent)
    }

    private fun deleteSlambook(slambook: SlambookEntry) {
        SlambookStorage.deleteSlambook(slambook)

        adapter.removeSlambook(slambook)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (slambooks.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
}
