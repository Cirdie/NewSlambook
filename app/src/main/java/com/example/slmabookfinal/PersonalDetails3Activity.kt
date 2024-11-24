package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails3Binding
import com.example.slmabookfinal.utils.ProgressDialog

class PersonalDetails3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button listener
        binding.backButton.setOnClickListener { finish() }

        // Continue button listener
        binding.continueButton.setOnClickListener {
            if (validateInputs()) navigateToNextStep()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate email
        val email = binding.emailInput.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailInput.error = "Email is required"
            isValid = false
        }

        // Validate phone
        val phone = binding.phoneInput.text.toString().trim()
        if (phone.isEmpty()) {
            binding.phoneInput.error = "Phone number is required"
            isValid = false
        }

        // Validate address
        val address = binding.addressInput.text.toString().trim()
        if (address.isEmpty()) {
            binding.addressInput.error = "Address is required"
            isValid = false
        }

        return isValid
    }

    private fun navigateToNextStep() {
        // Show custom progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Proceeding to the next step...")

        // Simulate delay for showing the dialog, then navigate
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss() // Dismiss the dialog
            val intent = Intent(this, HobbiesActivity::class.java) // Replace with the next activity
            startActivity(intent)
            finish() // Close the current activity
        }, 2000) // 2-second delay
    }
}
