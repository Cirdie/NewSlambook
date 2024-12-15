package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemQuestionBinding

class QuestionsAdapter(


    private val questions: MutableList<Question>,
    private val showRemoveButton: Boolean = false,
    private val itemSpacing: Int = 16,
    private val onUpdateRequested: (Question) -> Unit // Callback for update

) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    private var swipedPosition: Int? = null // Track the swiped position

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question, isSwiped: Boolean) {
            binding.questionText.text = question.questionText
            binding.answerText.text = question.answerText

            // Show or hide the remove and update buttons based on the swiped state
            binding.removeQuestionButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
            binding.updateQuestionButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
            // Add margin (spacing) between items
            val layoutParams = binding.questionContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, itemSpacing, 0, itemSpacing) // Top and Bottom spacing
            binding.questionContainer.layoutParams = layoutParams

            // Handle Remove button click
            binding.removeQuestionButton.setOnClickListener {
                removeItem(adapterPosition)
            }

            // Handle Update button click
            binding.updateQuestionButton.setOnClickListener {
                onUpdateRequested(question) // Trigger the update dialog for the selected question
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, swipedPosition == position) // Bind the swiped state
    }

    override fun getItemCount(): Int = questions.size

    // Add a new question to the list
    fun addQuestion(question: Question) {
        questions.add(question)
        notifyItemInserted(questions.size - 1)
        swipedPosition = null
    }

    // Remove an item at the specified position
    fun removeItem(position: Int) {
        if (position in questions.indices) {
            questions.removeAt(position)
            swipedPosition = null // Reset swipe state
            notifyItemRemoved(position)
        }
    }

    // Toggle the swiped state of an item
    fun toggleSwipe(position: Int) {
        val previousSwipedPosition = swipedPosition
        swipedPosition = if (swipedPosition == position) null else position // Toggle swipe state
        if (previousSwipedPosition != null) {
            notifyItemChanged(previousSwipedPosition) // Reset previous swiped item
        }
        notifyItemChanged(position) // Update newly swiped item
    }

    // Get the current list of questions
    fun getCurrentQuestions(): List<Question> = questions

    // Handle update of a single question (for example, open a dialog)
    fun updateQuestion(updatedQuestion: Question) {
        val position = questions.indexOfFirst { it.id == updatedQuestion.id }
        if (position != -1) {
            questions[position] = updatedQuestion
            notifyItemChanged(position)
        } else {
            // Optionally handle the case where the question wasn't found
        }
    }

    // Update the entire list of questions
    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear() // Clear the current list
        questions.addAll(newQuestions) // Add all new questions
        notifyDataSetChanged() // Notify the adapter that the entire dataset has changed
    }
}
