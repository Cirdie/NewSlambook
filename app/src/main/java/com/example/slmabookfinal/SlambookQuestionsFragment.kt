package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slmabookfinal.databinding.FragmentSlambookQuestionsBinding

class SlambookQuestionsFragment : Fragment() {

    private lateinit var binding: FragmentSlambookQuestionsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // Shared ViewModel
    private lateinit var questionsAdapter: QuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlambookQuestionsBinding.inflate(inflater, container, false)

        // Set up RecyclerView to display questions
        setupRecyclerView()

        // Observe the Slambook data from the SharedViewModel
        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null || slambook.questions.isEmpty()) {
                // If no questions, display the appropriate message
                binding.titleText.text = "No questions added yet"
            } else {
                // If questions are available, display them in RecyclerView
                displayQuestions(slambook)
            }
        })

        return binding.root
    }

    // Setup RecyclerView with QuestionsAdapter
    private fun setupRecyclerView() {
        // Initialize the adapter with an empty list (no action needed for update/remove in this fragment)
        questionsAdapter = QuestionsAdapter(mutableListOf()) {}

        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionsAdapter
        }
    }

    // Display the questions in the RecyclerView
    private fun displayQuestions(slambook: SlambookEntry) {
        // Update the adapter with the list of questions
        questionsAdapter.updateQuestions(slambook.questions)

        // Update the UI text based on whether there are questions or not
        binding.titleText.text = if (slambook.questions.isNotEmpty()) {
            "Your Questions"
        } else {
            "No questions added yet"
        }
    }
}
