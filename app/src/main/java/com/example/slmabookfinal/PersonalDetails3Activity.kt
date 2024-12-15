package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails3Binding
import com.example.slmabookfinal.utils.ProgressDialog

class PersonalDetails3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails3Binding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve SlambookEntry passed from the previous activity
        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()

        // Set dynamic welcome message
        binding.titleText.text = "Hello ${slambookEntry.firstName}!"

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        // Set up button listeners
        binding.continueButton.setOnClickListener { validateInputs() }
    }

    private fun validateInputs() {
        val email = binding.emailInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()
        val address = binding.addressInput.text.toString().trim()
        val facebook = binding.facebookInput.text.toString().trim()
        val instagram = binding.instagramInput.text.toString().trim()
        val twitter = binding.twitterInput.text.toString().trim()

        // Validate email format
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Please enter a valid email address"
            return
        }

        // Validate phone number (1 to 11 digits only)
        if (phone.isNotEmpty() && !phone.matches("^\\d{1,11}$".toRegex())) {
            binding.phoneInput.error = "Phone number should be 1 to 11 digits"
            return
        }

        // Validate address (Required field)
        if (address.isEmpty()) {
            binding.addressInput.error = "Address is required"
            return
        }

        // Save the inputs to SlambookEntry (Optional fields: Facebook, Instagram, Twitter)
        slambookEntry.email = email
        slambookEntry.phone = phone
        slambookEntry.address = address
        slambookEntry.facebookLink = facebook
        slambookEntry.instagramLink = instagram
        slambookEntry.twitterLink = twitter

        // Proceed to HobbiesActivity with the updated slambookEntry data
        proceedToHobbiesActivity()
    }

    private fun proceedToHobbiesActivity() {
        // Show progress dialog (optional)
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your data...")

        // Simulate saving process
        Handler(Looper.getMainLooper()).postDelayed({
            // Dismiss progress dialog after saving
            progressDialog.dismiss()

            // Pass the SlambookEntry to the HobbiesActivity
            val intent = Intent(this, HobbiesActivity::class.java).apply {
                putExtra("slambookEntry", slambookEntry)  // Passing data to next activity
            }
            startActivity(intent)
            finish()  // Finish current activity to move to the next one
        }, 2000) // Simulate 2-second delay
    }
}
