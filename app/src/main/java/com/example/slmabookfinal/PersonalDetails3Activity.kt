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

        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()

        binding.titleText.text = "Hello ${slambookEntry.firstName}!"

        progressDialog = ProgressDialog(this)

        binding.continueButton.setOnClickListener { validateInputs() }
    }

    private fun validateInputs() {
        val email = binding.emailInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()
        val address = binding.addressInput.text.toString().trim()
        val facebook = binding.facebookInput.text.toString().trim()
        val instagram = binding.instagramInput.text.toString().trim()
        val twitter = binding.twitterInput.text.toString().trim()

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Please enter a valid email address"
            return
        }

        if (phone.isNotEmpty() && !phone.matches("^\\d{1,11}$".toRegex())) {
            binding.phoneInput.error = "Phone number should be 1 to 11 digits"
            return
        }

        if (address.isEmpty()) {
            binding.addressInput.error = "Address is required"
            return
        }

        slambookEntry.email = email
        slambookEntry.phone = phone
        slambookEntry.address = address
        slambookEntry.facebookLink = facebook
        slambookEntry.instagramLink = instagram
        slambookEntry.twitterLink = twitter

        proceedToHobbiesActivity()
    }

    private fun proceedToHobbiesActivity() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your data...")

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()

            val intent = Intent(this, HobbiesActivity::class.java).apply {
                putExtra("slambookEntry", slambookEntry)  // Passing data to next activity
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
