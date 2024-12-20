package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slmabookfinal.databinding.FragmentAddQuestionDialogBinding

class AddQuestionDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddQuestionDialogBinding
    private var onQuestionAdded: ((String, String) -> Unit)? = null

    private val defaultQuestions = listOf(
        "What's your favorite quote or life motto?",
        "What's the last song you couldn't stop listening to?",
        "Who is your celebrity crush?",
        "What's your most embarrassing moment?",
        "If you were stranded on an island, what three things would you take?",
        "Custom Question"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener { dismiss() }

        setupSpinner()

        binding.addQuestionButton.setOnClickListener {
            handleAddQuestion()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            defaultQuestions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.questionSpinner.adapter = adapter

        binding.questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = defaultQuestions[position]
                binding.customQuestionInput.visibility = if (selected == "Custom Question") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handleAddQuestion() {
        val selectedQuestion = binding.questionSpinner.selectedItem.toString()
        val customQuestion = binding.customQuestionInput.text.toString().trim()
        val answer = binding.answerInput.text.toString().trim()

        val finalQuestion = if (selectedQuestion == "Custom Question" && customQuestion.isNotEmpty()) {
            customQuestion
        } else if (selectedQuestion != "Custom Question") {
            selectedQuestion
        } else {
            Toast.makeText(requireContext(), "Please enter a custom question", Toast.LENGTH_SHORT).show()
            return
        }

        if (answer.isNotEmpty()) {
            onQuestionAdded?.invoke(finalQuestion, answer)
            dismiss()
        } else {
            binding.answerInput.error = "Please provide an answer"
        }
    }

    companion object {
        const val TAG = "AddQuestionDialogFragment"

        fun newInstance(onQuestionAdded: (String, String) -> Unit): AddQuestionDialogFragment {
            return AddQuestionDialogFragment().apply {
                this.onQuestionAdded = onQuestionAdded
            }
        }
    }
}
