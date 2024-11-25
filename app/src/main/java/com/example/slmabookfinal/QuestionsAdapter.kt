package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemQuestionBinding

class QuestionsAdapter(
    private val questions: MutableList<Question>
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.questionText.text = question.questionText
            binding.answerText.text = question.answerText

            // Remove question logic
            binding.removeQuestionButton.setOnClickListener {
                removeQuestion(adapterPosition)
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

    fun addQuestion(question: Question) {
        questions.add(question)
        notifyItemInserted(questions.size - 1)
    }

    private fun removeQuestion(position: Int) {
        if (position in questions.indices) {
            questions.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getCurrentQuestions(): List<Question> = questions
}
