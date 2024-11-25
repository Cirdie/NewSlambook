package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
        "If you were stranded on an island, what three things would you take?"
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
            val selectedQuestion = binding.questionSpinner.selectedItem.toString()
            val customQuestion = binding.customQuestionInput.text.toString().trim()
            val answer = binding.answerInput.text.toString().trim()

            val finalQuestion = if (customQuestion.isNotEmpty()) customQuestion else selectedQuestion

            if (answer.isNotEmpty()) {
                onQuestionAdded?.invoke(finalQuestion, answer)
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
