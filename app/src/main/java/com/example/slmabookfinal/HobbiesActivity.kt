package com.example.slmabookfinal

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.service.quickaccesswallet.QuickAccessWalletService
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityHobbiesBinding
import com.example.slmabookfinal.utils.ProgressDialog

class HobbiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHobbiesBinding
    private val hobbiesAdapter: HobbiesAdapter by lazy {
        HobbiesAdapter(mutableListOf(), showRemoveButton = true) // Show Remove Button
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

            // Add ItemTouchHelper for swipe-to-delete functionality
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    hobbiesAdapter.toggleSwipe(position) // Handle the swipe
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
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
                slambookEntry.hobbies = hobbiesAdapter.getCurrentHobbies()

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
            alpha = if (hasHobbies) 1f else 0.5f
        }
    }

    private fun proceedToQuestionsActivity() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Saving your hobbies...")

        binding.proceedButton.isEnabled = false
        binding.proceedButton.alpha = 0.5f

        slambookEntry.hobbies = hobbiesAdapter.getCurrentHobbies()

        binding.proceedButton.postDelayed({
            progressDialog.dismiss()

            val intent = Intent(this, QuestionsActivity::class.java)
            intent.putExtra("slambookEntry", slambookEntry)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

