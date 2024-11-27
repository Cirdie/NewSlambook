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
        binding.backButton.setOnClickListener { finish() }
    }

    private fun validateInputs() {
        val email = binding.emailInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()
        val address = binding.addressInput.text.toString().trim()

        // Validate input fields
        when {
            email.isEmpty() -> {
                binding.emailInput.error = "Email is required"
                return
            }
            phone.isEmpty() -> {
                binding.phoneInput.error = "Phone is required"
                return
            }
            address.isEmpty() -> {
                binding.addressInput.error = "Address is required"
                return
            }
        }

        // Save the inputs to SlambookEntry
        slambookEntry.email = email
        slambookEntry.phone = phone
        slambookEntry.address = address

        // Proceed to save the data
        saveSlambookAndFinish()
    }

    private fun saveSlambookAndFinish() {
        // Add the completed entry to the repository
        SlambookRepository.addSlambook(slambookEntry)

        // Show progress dialog
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Finalizing...")

        // Simulate saving process
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()

            // Navigate back to the main menu (ChooseActivity)
            val intent = Intent(this, HobbiesActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Simulate delay
    }
}
