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
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var questionsAdapter: QuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlambookQuestionsBinding.inflate(inflater, container, false)

        // Set up the RecyclerView
        setupRecyclerView()

        // Observe the Slambook data from the SharedViewModel
        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null) {
                binding.questionsText.text = "No questions added yet"
            } else {
                displayQuestions(slambook)
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        // Initialize the adapter and pass the onQuestionsUpdated callback
        questionsAdapter = QuestionsAdapter(mutableListOf()) { updatedQuestions ->
            // This is the callback that will be triggered when questions are updated
            // You can handle any logic you need when the questions are updated, e.g., update the UI
            binding.questionsText.text = if (updatedQuestions.isEmpty()) {
                "No questions added yet"
            } else {
                "Your Questions"
            }
        }

        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionsAdapter
        }
    }

    private fun displayQuestions(slambook: SlambookEntry) {
        if (slambook.questions.isNotEmpty()) {
            questionsAdapter.updateQuestions(slambook.questions)
            binding.questionsText.text = "Your Questions"
        } else {
            binding.questionsText.text = "No questions added yet"
        }
    }
}
