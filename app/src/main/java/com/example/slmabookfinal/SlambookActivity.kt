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

        val slambooks = SlambookRepository.getSlambooks()

        if (slambooks.isNotEmpty()) {
            setupRecyclerView(slambooks)
        }

        binding.createSlambookButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
    }

    private fun setupRecyclerView(slambooks: List<SlambookEntry>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = SlambookListAdapter(slambooks) { selectedSlambook ->
            openSlambookDetails(selectedSlambook)
        }
    }

    private fun openSlambookDetails(slambook: SlambookEntry) {
        val intent = Intent(this, SlambookDetailsActivity::class.java).apply {
            putExtra("selectedSlambook", slambook)
        }
        startActivity(intent)
    }
}
