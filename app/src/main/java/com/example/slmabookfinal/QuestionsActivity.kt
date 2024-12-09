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
        QuestionsAdapter(mutableListOf(), showRemoveButton = true) { updatedQuestions ->
            // Handle saving the updated questions when they change
            slambookEntry.questions = updatedQuestions
        }
    }
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        // Retrieve SlambookEntry passed from the previous activity (including hobbies and any previous data)
        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()  // If null, initialize a new SlambookEntry

        // Log to verify the slambookEntry data (e.g., hobbies and existing questions)
        println("SlambookEntry Hobbies: ${slambookEntry.hobbies}")
        println("SlambookEntry Questions: ${slambookEntry.questions}")

        // Set up RecyclerView for displaying questions
        setupRecyclerView()

        // Set up "Add Question" button and "Proceed" button
        setupAddQuestionButton()
        setupProceedButton()

        // Update the Proceed button state based on questions entered
        updateProceedButtonState()
    }

    // Set up RecyclerView to display the questions
    private fun setupRecyclerView() {
        binding.questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionsActivity)
            adapter = questionsAdapter

            // Add spacing between items in RecyclerView
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = 16 // Spacing in dp
                    outRect.bottom = 16
                }
            })
        }

        // Observe changes in questions list to update the state of the Proceed button
        questionsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = updateProceedButtonState()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = updateProceedButtonState()
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = updateProceedButtonState()
        })
    }

    // Set up "Add Question" button to show the dialog for adding a question
    private fun setupAddQuestionButton() {
        binding.addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    // Set up "Proceed" button to handle saving and transitioning to the next activity
    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            if (questionsAdapter.getCurrentQuestions().isNotEmpty()) {
                // Save the current questions in the SlambookEntry
                slambookEntry.questions = questionsAdapter.getCurrentQuestions()

                // Proceed with the saving process
                showProgressDialogAndProceed()
            }
        }
    }

    // Show "Add Question" dialog when the user clicks the "Add Question" button
    private fun showAddQuestionDialog() {
        val addQuestionDialog = AddQuestionDialogFragment.newInstance { question, answer ->
            val newQuestion = Question(question, answer)
            questionsAdapter.addQuestion(newQuestion) // Add the new question to the RecyclerView
        }
        addQuestionDialog.show(supportFragmentManager, AddQuestionDialogFragment.TAG)
    }

    // Update the Proceed button's state based on whether questions have been added
    private fun updateProceedButtonState() {
        val hasQuestions = questionsAdapter.getCurrentQuestions().isNotEmpty()

        binding.proceedButton.apply {
            isEnabled = hasQuestions
            alpha = if (hasQuestions) 1f else 0.5f // Adjust transparency for disabled state
        }
    }

    private fun showProgressDialogAndProceed() {
        // Show progress dialog to indicate the saving process
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your questions...")

        // Disable the Proceed button to prevent multiple clicks during the saving process
        binding.proceedButton.isEnabled = false
        binding.proceedButton.alpha = 0.5f

        // Simulate a saving process delay (2 seconds in this case)
        binding.proceedButton.postDelayed({
            // Dismiss the progress dialog after the saving delay
            progressDialog.dismiss()

            // Save the updated SlambookEntry with questions (and hobbies) in the repository
            SlambookRepository.addSlambook(slambookEntry)

            // The updated SlambookEntry (with the new questions) will now be passed to the next activity
            val intent = Intent(this, ChooseActivity::class.java).apply {
                putExtra("slambookEntry", slambookEntry)  // Pass the entire updated SlambookEntry
            }
            startActivity(intent) // Start the ChooseActivity
            finish() // Close this activity
        }, 2000) // 2-second delay to simulate saving process
    }

}