package com.example.slmabookfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.slmabookfinal.databinding.FragmentSlambookProfileBinding

class SlambookProfileFragment : Fragment() {

    private lateinit var binding: FragmentSlambookProfileBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // Shared ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlambookProfileBinding.inflate(inflater, container, false)

        // Observe the slambook data from the ViewModel
        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null) {
                Log.e("SlambookProfileFragment", "No slambook data available")
                Toast.makeText(requireContext(), "No slambook data available", Toast.LENGTH_SHORT).show()
            } else {
                // Bind the data to the UI
                displayProfile(slambook)
            }
        })

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
