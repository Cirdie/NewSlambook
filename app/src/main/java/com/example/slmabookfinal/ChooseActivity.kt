package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding
    private var slambookEntry: SlambookEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve or mock the personalDetails object
        slambookEntry = intent.getSerializableExtra("personalDetails") as? SlambookEntry ?: SlambookEntry()

        // Set up click listeners
        binding.createButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        binding.listButton.setOnClickListener {
            val intent = Intent(this, SlambookActivity::class.java).apply {
                putExtra("personalDetails", slambookEntry) // Pass PersonalDetails to SlambookActivity
            }
            startActivity(intent)
        }
    }
}
