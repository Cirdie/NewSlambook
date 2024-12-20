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
    private val onUpdateRequested: (Question) -> Unit

) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    private var swipedPosition: Int? = null

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question, isSwiped: Boolean) {
            binding.questionText.text = question.questionText
            binding.answerText.text = question.answerText

            binding.removeQuestionButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
            binding.updateQuestionButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
            val layoutParams = binding.questionContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, itemSpacing, 0, itemSpacing) // Top and Bottom spacing
            binding.questionContainer.layoutParams = layoutParams

            binding.removeQuestionButton.setOnClickListener {
                removeItem(adapterPosition)
            }

            binding.updateQuestionButton.setOnClickListener {
                onUpdateRequested(question)
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

    fun addQuestion(question: Question) {
        questions.add(question)
        notifyItemInserted(questions.size - 1)
        swipedPosition = null
    }

    fun removeItem(position: Int) {
        if (position in questions.indices) {
            questions.removeAt(position)
            swipedPosition = null // Reset swipe state
            notifyItemRemoved(position)
        }
    }

    fun toggleSwipe(position: Int) {
        val previousSwipedPosition = swipedPosition
        swipedPosition = if (swipedPosition == position) null else position
        if (previousSwipedPosition != null) {
            notifyItemChanged(previousSwipedPosition)
        }
        notifyItemChanged(position)
    }

    fun getCurrentQuestions(): List<Question> = questions

    fun updateQuestion(updatedQuestion: Question) {
        val position = questions.indexOfFirst { it.id == updatedQuestion.id }
        if (position != -1) {
            questions[position] = updatedQuestion
            notifyItemChanged(position)
        } else {
        }
    }

    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear() // Clear the current list
        questions.addAll(newQuestions) // Add all new questions
        notifyDataSetChanged() // Notify the adapter that the entire dataset has changed
    }
}
