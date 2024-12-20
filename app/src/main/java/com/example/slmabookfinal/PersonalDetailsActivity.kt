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
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    private val AVATAR_PLACEHOLDER_ID = R.drawable.avatar_placeholder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()

        progressDialog = ProgressDialog(this)

        if (slambookEntry.avatarId != 0) {
            binding.avatarImageView.setImageResource(slambookEntry.avatarId)
        } else {
            binding.avatarImageView.setImageResource(AVATAR_PLACEHOLDER_ID)
            slambookEntry.avatarId = AVATAR_PLACEHOLDER_ID
        }

        binding.changeAvatarButton.setOnClickListener { openAvatarSelection() }
        binding.continueButton.setOnClickListener { validateInputs() }
    }

    private fun validateInputs() {
        val firstName = binding.firstNameInput.text.toString().trim()
        val lastName = binding.lastNameInput.text.toString().trim()
        val nickname = binding.nicknameInput.text.toString().trim()

        when {
            firstName.isEmpty() -> {
                binding.firstNameInput.error = "First name is required"
                return
            }
            lastName.isEmpty() -> {
                binding.lastNameInput.error = "Last name is required"
                return
            }
            nickname.isEmpty() -> {
                binding.nicknameInput.error = "Nickname is required"
                return
            }
            slambookEntry.avatarId == AVATAR_PLACEHOLDER_ID -> {  // Check if placeholder avatar is selected
                progressDialog.show(ProgressDialog.DialogType.ERROR, "Please select a valid avatar.")

                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                }, 2000)

                return
            }
            else -> {
                slambookEntry.firstName = firstName
                slambookEntry.lastName = lastName
                slambookEntry.nickname = nickname

                showProgressAndNavigate()
            }
        }
    }

    private fun openAvatarSelection() {
        val intent = Intent(this, AvatarSelectionActivity::class.java)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val avatarId = data?.getIntExtra("selectedAvatar", -1)
            if (avatarId != null && avatarId != -1) {
                slambookEntry.avatarId = avatarId
                binding.avatarImageView.setImageResource(avatarId)
            }
        }
    }

    private fun showProgressAndNavigate() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving details...")
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()

            val intent = Intent(this, PersonalDetails2Activity::class.java).apply {
                putExtra("slambookEntry", slambookEntry)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
