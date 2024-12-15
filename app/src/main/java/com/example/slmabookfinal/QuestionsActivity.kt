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

    // Adapter for managing the list of questions
    private val questionsAdapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf(), showRemoveButton = true) { question ->
            showUpdateQuestionDialog(question) // Trigger update dialog when update button is clicked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ProgressDialog
        progressDialog = ProgressDialog(this)

        // Retrieve SlambookEntry from the intent
        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry ?: SlambookEntry()

        // Set up RecyclerView for displaying questions
        setupRecyclerView()

        // Initialize buttons (Add Question and Proceed)
        setupAddQuestionButton()
        setupProceedButton()

        // Update Proceed button state based on questions entered
        updateProceedButtonState()
    }

    // Setup RecyclerView for displaying questions
    private fun setupRecyclerView() {
        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionsActivity)
            adapter = questionsAdapter

            // Add ItemTouchHelper for swipe-to-reveal delete/update buttons
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                // Handle swipe event to toggle delete/update button visibility
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    questionsAdapter.toggleSwipe(position)
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
        }

        // Observe changes to the questions list and update the Proceed button state accordingly
        questionsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = updateProceedButtonState()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = updateProceedButtonState()
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = updateProceedButtonState()
        })
    }

    // Setup the "Add Question" button
    private fun setupAddQuestionButton() {
        binding.addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    // Setup the "Proceed" button
    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            val currentQuestions = questionsAdapter.getCurrentQuestions()
            if (currentQuestions.isNotEmpty()) {
                slambookEntry.questions = currentQuestions // Save the questions in the SlambookEntry
                showProgressDialogAndProceed() // Proceed with saving the data
            } else {
                // Provide feedback if no questions have been added
                showToast("Please add at least one question.")
            }
        }
    }

    // Show the dialog to add a new question
    private fun showAddQuestionDialog() {
        val newQuestionId = (questionsAdapter.getCurrentQuestions().size + 1)

        val addQuestionDialog = AddQuestionDialogFragment.newInstance { question, answer ->
            val newQuestion = Question(id = newQuestionId, questionText = question, answerText = answer)
            questionsAdapter.addQuestion(newQuestion) // Add the new question to the RecyclerView
        }

        addQuestionDialog.show(supportFragmentManager, AddQuestionDialogFragment.TAG)
    }

    // Show the dialog to update an existing question
    private fun showUpdateQuestionDialog(question: Question) {
        val updateQuestionDialog = UpdateQuestionDialogFragment.newInstance(question) { updatedQuestion ->
            questionsAdapter.updateQuestion(updatedQuestion) // Update the question in the RecyclerView
        }
        updateQuestionDialog.show(supportFragmentManager, UpdateQuestionDialogFragment.TAG)
    }

    // Update the "Proceed" button state (enabled or disabled) based on whether there are questions
    private fun updateProceedButtonState() {
        val hasQuestions = questionsAdapter.getCurrentQuestions().isNotEmpty()

        binding.proceedButton.apply {
            isEnabled = hasQuestions
            alpha = if (hasQuestions) 1f else 0.5f // Adjust transparency for the disabled state
        }
    }

    // Show the progress dialog and proceed with saving the questions
    private fun showProgressDialogAndProceed() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your questions...")

        // Disable the Proceed button during the saving process to prevent multiple clicks
        binding.proceedButton.isEnabled = false
        binding.proceedButton.alpha = 0.5f

        // Simulate a delay (2 seconds) to represent the saving process
        binding.proceedButton.postDelayed({
            progressDialog.dismiss() // Dismiss the progress dialog

            // Proceed to the next activity and pass the updated SlambookEntry
            val intent = Intent(this, FavoritesActivity::class.java).apply {
                putExtra("slambookEntry", slambookEntry) // Pass the updated SlambookEntry
            }
            startActivity(intent) // Start the FavoritesActivity
            finish() // Close the current activity
        }, 2000) // 2-second delay for the simulation
    }

    // Show a Toast message for feedback
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
