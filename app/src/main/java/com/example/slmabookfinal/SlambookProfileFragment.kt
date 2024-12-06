package com.example.slmabookfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.slmabookfinal.databinding.FragmentSlambookProfileBinding

class SlambookProfileFragment : Fragment() {

    private lateinit var binding: FragmentSlambookProfileBinding
    private var slambook: SlambookEntry? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlambookProfileBinding.inflate(inflater, container, false)

        // Retrieve the slambook entry passed in the arguments
        slambook = arguments?.getSerializable("slambook") as? SlambookEntry

        // If no slambook data is passed, show a toast message and log it
        if (slambook == null) {
            Log.e("SlambookProfileFragment", "No slambook data passed to the fragment")
            Toast.makeText(requireContext(), "No slambook data available", Toast.LENGTH_SHORT).show()
        } else {
            // If slambook is not null, bind the data to the views
            displayProfile(slambook!!)
        }

        return binding.root
    }

    private fun displayProfile(slambook: SlambookEntry) {
        // Bind the slambook data to the UI components in the layout
        binding.profileImage.setImageResource(slambook.avatarId)
        binding.profileName.text = "${slambook.firstName} ${slambook.lastName}"
        binding.profileNicknameValue.text = slambook.nickname ?: "N/A"
        binding.profileEmailValue.text = slambook.email ?: "N/A"
        binding.profilePhoneValue.text = slambook.phone ?: "N/A"
        binding.genderValue.text = slambook.gender ?: "N/A"
        binding.birthdateValue.text = slambook.birthDate?.let {
            "${it.second}/${it.first}/${it.third}"
        } ?: "N/A"
        binding.ageValue.text = slambook.age?.toString() ?: "N/A"
        binding.weightValue.text = slambook.weight ?: "N/A"
        binding.heightValue.text = slambook.height ?: "N/A"
        binding.addressValue.text = slambook.address ?: "N/A"
        binding.facebookUrl.text = slambook.facebookLink ?: "N/A"
        binding.instagramUrl.text = slambook.instagramLink ?: "N/A"
        binding.twitterUrl.text = slambook.twitterLink ?: "N/A"
    }
}


