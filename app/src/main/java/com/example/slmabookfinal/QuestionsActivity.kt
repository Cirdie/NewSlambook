package com.example.slmabookfinal

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityQuestionsBinding

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding
    private val questionsAdapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddQuestionButton()
        setupProceedButton()
    }

    private fun setupRecyclerView() {
        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionsActivity)
            adapter = questionsAdapter

            // Add spacing between items
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = 16 // Spacing in dp
                    outRect.bottom = 16
                }
            })
        }
    }

    private fun setupAddQuestionButton() {
        binding.addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }


    private fun showAddQuestionDialog() {
        val addQuestionDialog = AddQuestionDialogFragment.newInstance { question, answer ->
            val newQuestion = Question(question, answer)
            questionsAdapter.addQuestion(newQuestion) // Add the new question to the RecyclerView
        }
        addQuestionDialog.show(supportFragmentManager, AddQuestionDialogFragment.TAG)
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
