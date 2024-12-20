package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slmabookfinal.databinding.FragmentSlambookHobbiesBinding

class SlambookHobbiesFragment : Fragment() {

    private lateinit var binding: FragmentSlambookHobbiesBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // Shared ViewModel
    private lateinit var hobbiesAdapter: HobbiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlambookHobbiesBinding.inflate(inflater, container, false)

        setupRecyclerView()

        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null) {
                binding.hobbiesTitle.text = "No hobbies selected yet"
            } else {
                displayHobbies(slambook)
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        hobbiesAdapter = HobbiesAdapter(mutableListOf(), showRemoveButton = false)

        binding.hobbiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hobbiesAdapter
        }
    }

    private fun displayHobbies(slambook: SlambookEntry) {
        if (slambook.hobbies.isNotEmpty()) {
            hobbiesAdapter.updateHobbies(slambook.hobbies)
            binding.hobbiesTitle.text = "Your Hobbies"
        } else {
            binding.hobbiesTitle.text = "No hobbies selected yet"
        }
    }
}
