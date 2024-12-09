package com.example.slmabookfinal

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.service.quickaccesswallet.QuickAccessWalletService
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityHobbiesBinding
import com.example.slmabookfinal.utils.ProgressDialog

class HobbiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHobbiesBinding
    private val hobbiesAdapter: HobbiesAdapter by lazy {
        HobbiesAdapter(mutableListOf(), isSelectable = true, showRemoveButton = true) // Show Remove Button
    }
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHobbiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)

        // Retrieve SlambookEntry passed from previous activity
        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry() // If it's null, create a new one

        setupRecyclerView()
        setupAddHobbyButton()
        setupProceedButton()

        // Set initial state of the Proceed button
        updateProceedButtonState()
    }

    private fun setupRecyclerView() {
        binding.hobbiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HobbiesActivity)
            adapter = hobbiesAdapter

            // Add spacing between items
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = 16 // Spacing in dp
                    outRect.bottom = 16
                }
            })
        }

        // Observe adapter data changes to update Proceed button state
        hobbiesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
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

    private fun setupAddHobbyButton() {
        binding.addHobbyButton.setOnClickListener {
            showAddHobbyDialog()
        }
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            if (hobbiesAdapter.getCurrentHobbies().isNotEmpty()) {
                // Save the selected hobbies in SlambookEntry
                slambookEntry.hobbies = hobbiesAdapter.getSelectedHobbies()

                // Proceed to the next activity, passing the updated SlambookEntry (no repository save yet)
                proceedToQuestionsActivity()
            }
        }
    }

    private fun showAddHobbyDialog() {
        val addHobbyDialog = AddHobbyDialogFragment.newInstance { newHobby ->
            hobbiesAdapter.addHobby(newHobby) // Add the new hobby to the RecyclerView
        }
        addHobbyDialog.show(supportFragmentManager, AddHobbyDialogFragment.TAG)
    }

    private fun updateProceedButtonState() {
        val hasHobbies = hobbiesAdapter.getCurrentHobbies().isNotEmpty()

        binding.proceedButton.apply {
            isEnabled = hasHobbies
            alpha = if (hasHobbies) 1f else 0.5f // Adjust transparency
        }
    }

    private fun proceedToQuestionsActivity() {
        // Show the progress dialog to indicate the process
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your hobbies...")

        // Disable the proceed button to prevent multiple clicks during the process
        binding.proceedButton.isEnabled = false
        binding.proceedButton.alpha = 0.5f

        // Update the SlambookEntry with the selected hobbies from the adapter
        slambookEntry.hobbies = hobbiesAdapter.getCurrentHobbies()

        // Simulate a saving process delay (2 seconds in this case)
        binding.proceedButton.postDelayed({
            // Dismiss the progress dialog after the saving delay
            progressDialog.dismiss()

            // Pass the SlambookEntry to QuestionsActivity without saving it in the repository
            val intent = Intent(this, QuestionsActivity::class.java)
            intent.putExtra("slambookEntry", slambookEntry) // Pass data to the next activity
            startActivity(intent)
            finish() // Close this activity
        }, 2000) // 2-second delay to simulate the saving process
    }
}
