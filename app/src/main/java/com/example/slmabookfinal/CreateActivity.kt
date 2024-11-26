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

        // Set up button listeners
        setupListeners()
    }

    private fun setupListeners() {
        binding.createButton.setOnClickListener {
            if (validateInputs()) {
                performCreation()
            }
        }

        binding.cancelButton.setOnClickListener {
            cancelCreation()
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
        val slambookName = binding.slambookNameInput.text.toString().trim()
        val tagline = binding.taglineInput.text.toString().trim()
        val slambook = Slambook(name = slambookName, tagline = tagline) // Create Slambook object

        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Creating your Slambook...")

        // Simulate creation process
        Handler(Looper.getMainLooper()).postDelayed({
            val isSuccess = true // Simulate success or failure

            if (isSuccess) {
                onCreationSuccess(slambook)
            } else {
                onCreationFailure()
            }
        }, 3000) // Simulated delay for creation
    }

    private fun cancelCreation() {
        progressDialog.show(ProgressDialog.DialogType.ERROR, "Action Cancelled.")
        Handler(Looper.getMainLooper()).postDelayed({ progressDialog.dismiss() }, 2000)
    }

    private fun onCreationSuccess(slambook: Slambook) {
        progressDialog.show(ProgressDialog.DialogType.SUCCESS, "Slambook Created Successfully!")

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this, PersonalDetailsActivity::class.java).apply {
                putExtra("slambook", slambook) // Pass the Slambook object
            }
            startActivity(intent)
            finish()
        }, 2000) // Short delay to allow success message to display
    }

    private fun onCreationFailure() {
        progressDialog.show(ProgressDialog.DialogType.ERROR, "Failed to create Slambook.")
        Handler(Looper.getMainLooper()).postDelayed({ progressDialog.dismiss() }, 2000)
    }
}
