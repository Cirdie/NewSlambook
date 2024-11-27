package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityCreateBinding
import com.example.slmabookfinal.utils.ProgressDialog

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.createButton.setOnClickListener {
            if (validateInputs()) {
                proceedToPersonalDetails()
            }
        }
        binding.cancelButton.setOnClickListener {
            progressDialog.show(ProgressDialog.DialogType.ERROR, "Cancelled.")
            Handler(Looper.getMainLooper()).postDelayed({ progressDialog.dismiss() }, 2000)
        }
    }

    private fun validateInputs(): Boolean {
        val slambookName = binding.slambookNameInput.text.toString().trim()
        val tagline = binding.taglineInput.text.toString().trim()

        var isValid = true

        if (slambookName.isEmpty()) {
            binding.slambookNameInput.error = "Name is required"
            isValid = false
        }
        if (tagline.isEmpty()) {
            binding.taglineInput.error = "Tagline is required"
            isValid = false
        }
        return isValid
    }

    private fun proceedToPersonalDetails() {
        val slambookName = binding.slambookNameInput.text.toString().trim()
        val tagline = binding.taglineInput.text.toString().trim()

        val slambookEntry = SlambookEntry(
            slambookName = slambookName,
            slambookTagline = tagline
        )

        val intent = Intent(this, PersonalDetailsActivity::class.java).apply {
            putExtra("slambookEntry", slambookEntry)
        }
        startActivity(intent)
        finish()
    }
}
