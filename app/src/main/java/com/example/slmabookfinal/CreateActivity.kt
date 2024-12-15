package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityCreateBinding
import com.example.slmabookfinal.utils.ProgressDialog

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) 
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
            // Show a progress dialog indicating the cancellation
            progressDialog.show(ProgressDialog.DialogType.ERROR, "Creating of Slambook Cancelled")

            // Delay to dismiss the dialog and navigate back
            Handler(Looper.getMainLooper()).postDelayed({
                progressDialog.dismiss()
                finish()
            }, 2000) // Dismiss after 2 seconds
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
