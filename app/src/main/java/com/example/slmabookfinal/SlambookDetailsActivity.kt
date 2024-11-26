package com.example.slmabookfinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivitySlambookDetailsBinding

class SlambookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlambookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlambookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slambook = intent.getSerializableExtra("selectedSlambook") as? SlambookEntry
        if (slambook != null) {
            displayDetails(slambook)
        }
    }

    private fun displayDetails(details: SlambookEntry) {
        with(binding) {
            avatarImageView.setImageResource(details.avatarId)
            nameTextView.text = "Name: ${details.firstName} ${details.lastName}"
            nicknameTextView.text = "Nickname: ${details.nickname}"
            genderTextView.text = "Gender: ${details.gender ?: "N/A"}"
            ageTextView.text = "Age: ${details.age ?: "N/A"}"
            birthDateTextView.text = "Birthdate: ${
                details.birthDate?.let { "${it.third}/${it.second}/${it.first}" } ?: "N/A"
            }"
            weightTextView.text = "Weight: ${details.weight} kg"
            heightTextView.text = "Height: ${details.height} cm"
            emailTextView.text = "Email: ${details.email}"
            phoneTextView.text = "Phone: ${details.phone}"
            addressTextView.text = "Address: ${details.address}"
            facebookTextView.text = "Facebook: ${details.facebookLink ?: "N/A"}"
            instagramTextView.text = "Instagram: ${details.instagramLink ?: "N/A"}"
            twitterTextView.text = "Twitter: ${details.twitterLink ?: "N/A"}"
        }
    }
}
