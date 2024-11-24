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

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        binding.createButton.setOnClickListener {
            if (validateInputs()) performCreation()
        }

        binding.cancelButton.setOnClickListener {
            progressDialog.show(ProgressDialog.DialogType.ERROR, "Action Cancelled.")
            Handler(Looper.getMainLooper()).postDelayed({ progressDialog.dismiss() }, 2000)
        }
    }

    private fun validateInputs(): Boolean {
        val slambookName = binding.slambookNameInput.text.toString().trim()
        val tagline = binding.taglineInput.text.toString().trim()

        var isValid = true

        if (slambookName.isEmpty()) {
            binding.slambookNameInput.error = "Slambook name cannot be empty"
            isValid = false
        } else {
            binding.slambookNameInput.error = null
        }

        if (tagline.isEmpty()) {
            binding.taglineInput.error = "Tagline cannot be empty"
            isValid = false
        } else {
            binding.taglineInput.error = null
        }

        return isValid
    }

    private fun performCreation() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Creating your Slambook...")

        Handler(Looper.getMainLooper()).postDelayed({
            val isSuccess = true // Simulate success or failure
            if (isSuccess) {
                progressDialog.show(ProgressDialog.DialogType.SUCCESS, "Slambook Created Successfully!")

                // Navigate to PersonalDetailsActivity after success
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    val intent = Intent(this, PersonalDetailsActivity::class.java)
                    startActivity(intent)
                    finish() // Close the current activity
                }, 2000)
            } else {
                progressDialog.show(ProgressDialog.DialogType.ERROR, "Failed to create Slambook.")
                Handler(Looper.getMainLooper()).postDelayed({ progressDialog.dismiss() }, 2000)
            }
        }, 3000) // Simulate 3-second delay
    }
}
