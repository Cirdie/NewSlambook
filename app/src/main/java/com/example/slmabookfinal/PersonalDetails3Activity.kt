package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails3Binding
import com.example.slmabookfinal.utils.ProgressDialog

class PersonalDetails3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails3Binding
    private lateinit var personalDetails: PersonalDetails
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        // Retrieve the personal details object
        personalDetails = intent.getSerializableExtra("personalDetails") as? PersonalDetails
            ?: run {
                finish() // Close activity if data is missing
                return
            }

        // Set the welcome title with the first name
        binding.titleText.text = "Hello ${personalDetails.firstName}!"

        // Back button listener
        binding.backButton.setOnClickListener { finish() }

        // Continue button listener
        binding.continueButton.setOnClickListener {
            if (validateInputs()) {
                showProgressAndNavigate()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate email
        val email = binding.emailInput.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailInput.error = "Email is required"
            isValid = false
        } else {
            personalDetails.email = email
        }

        // Validate phone
        val phone = binding.phoneInput.text.toString().trim()
        if (phone.isEmpty()) {
            binding.phoneInput.error = "Phone number is required"
            isValid = false
        } else {
            personalDetails.phone = phone
        }

        // Validate address
        val address = binding.addressInput.text.toString().trim()
        if (address.isEmpty()) {
            binding.addressInput.error = "Address is required"
            isValid = false
        } else {
            personalDetails.address = address
        }

        // Optional social media links
        personalDetails.facebookLink = binding.facebookInput.text.toString().trim().takeIf { it.isNotEmpty() }
        personalDetails.instagramLink = binding.instagramInput.text.toString().trim().takeIf { it.isNotEmpty() }
        personalDetails.twitterLink = binding.twitterInput.text.toString().trim().takeIf { it.isNotEmpty() }

        return isValid
    }

    private fun showProgressAndNavigate() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your details...")

        // Simulate delay to show the progress dialog
        binding.continueButton.isEnabled = false
        binding.backButton.isEnabled = false
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Proceeding...")

        // Simulate delay
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your details...")
        Handler(mainLooper).postDelayed({
            progressDialog.dismiss()
            navigateToNextStep()
        }, 2000) // 2 seconds
    }

    private fun navigateToNextStep() {
        val intent = Intent(this, HobbiesActivity::class.java)
        intent.putExtra("personalDetails", personalDetails) // Pass the updated details
        startActivity(intent)
        finish() // Close the current activity
    }
}
