package com.example.slmabookfinal

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityQuestionsBinding
import com.example.slmabookfinal.utils.ProgressDialog

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding
    private val questionsAdapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf())
    }
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        setupRecyclerView()
        setupAddQuestionButton()
        setupProceedButton()

        // Set initial state of the Proceed button
        updateProceedButtonState()
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

        // Observe adapter data changes to update Proceed button state
        questionsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                updateProceedButtonState()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                updateProceedButtonState()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                updateProceedButtonState()
            }
        })
    }

    private fun setupAddQuestionButton() {
        binding.addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            if (questionsAdapter.getCurrentQuestions().isNotEmpty()) {
                showProgressDialogAndProceed()
            }
        }
    }

    private fun showAddQuestionDialog() {
        val addQuestionDialog = AddQuestionDialogFragment.newInstance { question, answer ->
            val newQuestion = Question(question, answer)
            questionsAdapter.addQuestion(newQuestion) // Add the new question to the RecyclerView
        }
        addQuestionDialog.show(supportFragmentManager, AddQuestionDialogFragment.TAG)
    }

    private fun updateProceedButtonState() {
        val hasQuestions = questionsAdapter.itemCount > 0

        binding.proceedButton.apply {
            isEnabled = hasQuestions
            alpha = if (hasQuestions) 1f else 0.5f // Adjust transparency
        }
    }

    private fun showProgressDialogAndProceed() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your questions...")

        binding.proceedButton.isEnabled = false // Prevent multiple clicks
        binding.proceedButton.alpha = 0.5f

        // Simulate a delay for saving questions
        binding.proceedButton.postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish() // Close this activity
        }, 2000) // 2-second delay
    }
}
