package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetailsBinding
import com.example.slmabookfinal.utils.ProgressDialog

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetailsBinding
    private var selectedAvatar: Int = R.drawable.avatar1 // Set default avatar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default avatar
        binding.avatarImageView.setImageResource(selectedAvatar)

        // Handle avatar change
        binding.changeAvatarButton.setOnClickListener {
            openAvatarSelection()
        }

        // Handle continue button
        binding.continueButton.setOnClickListener {
            validateInputs()
        }
    }

    private fun validateInputs() {
        val firstName = binding.firstNameInput.text.toString().trim()
        val lastName = binding.lastNameInput.text.toString().trim()
        val nickname = binding.nicknameInput.text.toString().trim()

        when {
            firstName.isEmpty() -> {
                binding.firstNameInput.error = "First name is required"
                binding.firstNameInput.requestFocus()
            }
            lastName.isEmpty() -> {
                binding.lastNameInput.error = "Last name is required"
                binding.lastNameInput.requestFocus()
            }
            nickname.isEmpty() -> {
                binding.nicknameInput.error = "Nickname is required"
                binding.nicknameInput.requestFocus()
            }
            else -> {
                navigateToNextStep()
            }
        }
    }

    private fun openAvatarSelection() {
        val intent = Intent(this, AvatarSelectionActivity::class.java)
        startActivityForResult(intent, 100) // Request code for avatar selection
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val avatarId = data?.getIntExtra("selectedAvatar", -1)
            if (avatarId != null && avatarId != -1) {
                selectedAvatar = avatarId
                binding.avatarImageView.setImageResource(avatarId)
            }
        }
    }

    private fun navigateToNextStep() {
        val progressDialog = ProgressDialog(this)
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Proceeding to the next step...")

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this, PersonalDetails2Activity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
