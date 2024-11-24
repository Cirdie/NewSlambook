package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding // View Binding for uniform structure

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listeners for buttons
        binding.createButton.setOnClickListener {
            // Navigate to Create Activity
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        binding.listButton.setOnClickListener {
            // Navigate to My Slam Book Activity
            val intent = Intent(this, MySlamBookActivity::class.java)
            startActivity(intent)
        }
    }
}
