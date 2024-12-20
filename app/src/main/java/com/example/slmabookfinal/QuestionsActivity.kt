package com.example.slmabookfinal

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityQuestionsBinding
import com.example.slmabookfinal.utils.ProgressDialog

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    private val questionsAdapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf(), showRemoveButton = true) { question ->
            showUpdateQuestionDialog(question) // Trigger update dialog when update button is clicked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry ?: SlambookEntry()

        setupRecyclerView()

        setupAddQuestionButton()
        setupProceedButton()

        updateProceedButtonState()
    }

    private fun setupRecyclerView() {
        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionsActivity)
            adapter = questionsAdapter

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    questionsAdapter.toggleSwipe(position)
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
        }

        questionsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = updateProceedButtonState()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = updateProceedButtonState()
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = updateProceedButtonState()
        })
    }

    private fun setupAddQuestionButton() {
        binding.addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            val currentQuestions = questionsAdapter.getCurrentQuestions()
            if (currentQuestions.isNotEmpty()) {
                slambookEntry.questions = currentQuestions // Save the questions in the SlambookEntry
                showProgressDialogAndProceed() // Proceed with saving the data
            } else {
                showToast("Please add at least one question.")
            }
        }
    }

    private fun showAddQuestionDialog() {
        val newQuestionId = (questionsAdapter.getCurrentQuestions().size + 1)

        val addQuestionDialog = AddQuestionDialogFragment.newInstance { question, answer ->
            val newQuestion = Question(id = newQuestionId, questionText = question, answerText = answer)
            questionsAdapter.addQuestion(newQuestion) // Add the new question to the RecyclerView
        }

        addQuestionDialog.show(supportFragmentManager, AddQuestionDialogFragment.TAG)
    }

    private fun showUpdateQuestionDialog(question: Question) {
        val updateQuestionDialog = UpdateQuestionDialogFragment.newInstance(question) { updatedQuestion ->
            questionsAdapter.updateQuestion(updatedQuestion) // Update the question in the RecyclerView
        }
        updateQuestionDialog.show(supportFragmentManager, UpdateQuestionDialogFragment.TAG)
    }

    private fun updateProceedButtonState() {
        val hasQuestions = questionsAdapter.getCurrentQuestions().isNotEmpty()

        binding.proceedButton.apply {
            isEnabled = hasQuestions
            alpha = if (hasQuestions) 1f else 0.5f // Adjust transparency for the disabled state
        }
    }

    private fun showProgressDialogAndProceed() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your questions...")

        binding.proceedButton.isEnabled = false
        binding.proceedButton.alpha = 0.5f

        binding.proceedButton.postDelayed({
            progressDialog.dismiss()

            val intent = Intent(this, FavoritesActivity::class.java).apply {
                putExtra("slambookEntry", slambookEntry)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
