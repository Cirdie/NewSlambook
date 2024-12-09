package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemQuestionBinding

class QuestionsAdapter(
    private val questions: MutableList<Question>,
    private val showRemoveButton: Boolean = false,
    private val onQuestionsUpdated: (List<Question>) -> Unit // Callback to notify when questions are updated
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.questionText.text = question.questionText
            binding.answerText.text = question.answerText

            // Show or hide the remove button based on the flag
            if (showRemoveButton) {
                binding.removeQuestionButton.visibility = View.VISIBLE
                binding.removeQuestionButton.setOnClickListener {
                    removeQuestion(adapterPosition)
                }
            } else {
                binding.removeQuestionButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    // Add a new question to the list
    fun addQuestion(question: Question) {
        questions.add(question)
        notifyItemInserted(questions.size - 1)  // Notify the adapter about the new item
        onQuestionsUpdated(questions) // Notify the activity or fragment that questions have been updated
    }

    // Remove a question at the specified position
    private fun removeQuestion(position: Int) {
        if (position in questions.indices) {
            questions.removeAt(position)
            notifyItemRemoved(position)  // Notify the adapter that an item was removed
            onQuestionsUpdated(questions) // Notify the activity or fragment that questions have been updated
        }
    }

    // Get the current list of questions
    fun getCurrentQuestions(): List<Question> = questions

    // Update the questions list and notify the adapter
    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear()  // Clear the current list of questions
        questions.addAll(newQuestions)  // Add the new questions
        notifyDataSetChanged()  // Notify that the data has changed
        onQuestionsUpdated(questions) // Notify the activity or fragment that questions have been updated
    }
}
