package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slmabookfinal.databinding.FragmentUpdateQuestionDialogBinding

class UpdateQuestionDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentUpdateQuestionDialogBinding
    private var onQuestionUpdated: ((Question) -> Unit)? = null
    private lateinit var question: Question

    private val defaultQuestions = listOf(
        "What's your favorite quote or life motto?",
        "What's the last song you couldn't stop listening to?",
        "Who is your celebrity crush?",
        "What's your most embarrassing moment?",
        "If you were stranded on an island, what three things would you take?",
        "Custom Question" // Custom option
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateQuestionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the passed Question object
        question = arguments?.getSerializable("question") as Question

        // Initialize the UI with current question data
        setupSpinner()

        // Set the answer input field to current answer value
        binding.answerInput.setText(question.answerText)

        // Close the dialog
        binding.closeButton.setOnClickListener { dismiss() }

        // Handle the update logic
        binding.updateButton.setOnClickListener {
            val selectedQuestion = binding.questionSpinner.selectedItem.toString()
            val customQuestion = binding.customQuestionInput.text.toString().trim()
            val updatedAnswer = binding.answerInput.text.toString().trim()

            // Determine the final question text
            val finalQuestion = if (selectedQuestion == "Custom Question" && customQuestion.isNotEmpty()) {
                customQuestion
            } else if (selectedQuestion != "Custom Question") {
                selectedQuestion
            } else {
                Toast.makeText(requireContext(), "Please enter a custom question", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (updatedAnswer.isNotEmpty()) {
                val updatedQuestion = question.copy(questionText = finalQuestion, answerText = updatedAnswer)
                onQuestionUpdated?.invoke(updatedQuestion)
                dismiss()
            } else {
                binding.answerInput.error = "Please provide an answer"
            }
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

        // Preselect the current question in the spinner
        val currentQuestion = question.questionText
        val position = defaultQuestions.indexOf(currentQuestion).takeIf { it >= 0 } ?: defaultQuestions.size - 1
        binding.questionSpinner.setSelection(position)

        // Show custom question input if the "Custom Question" is selected
        binding.questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = defaultQuestions[position]
                binding.customQuestionInput.visibility = if (selected == "Custom Question") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        const val TAG = "UpdateQuestionDialogFragment"

        fun newInstance(question: Question, onQuestionUpdated: (Question) -> Unit): UpdateQuestionDialogFragment {
            return UpdateQuestionDialogFragment().apply {
                this.onQuestionUpdated = onQuestionUpdated
                val args = Bundle()
                args.putSerializable("question", question) // Pass the question object
                arguments = args
            }
        }
    }
}
