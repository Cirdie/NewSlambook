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

        setupRecyclerView()

        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null || slambook.questions.isEmpty()) {
                binding.titleText.text = "No questions added yet"
            } else {
                displayQuestions(slambook)
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        questionsAdapter = QuestionsAdapter(mutableListOf()) {}

        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionsAdapter
        }
    }

    private fun displayQuestions(slambook: SlambookEntry) {
        questionsAdapter.updateQuestions(slambook.questions)

        binding.titleText.text = if (slambook.questions.isNotEmpty()) {
            "Your Questions"
        } else {
            "No questions added yet"
        }
    }
}
