package com.example.slmabookfinal

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityHobbiesBinding

class HobbiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHobbiesBinding
    private val hobbiesAdapter: HobbiesAdapter by lazy {
        HobbiesAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHobbiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddHobbyButton()
    }

    private fun setupRecyclerView() {
        binding.hobbiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HobbiesActivity)
            adapter = hobbiesAdapter

            // Add spacing between items
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = 16
                    outRect.bottom = 16
                }
            })
        }
    }

    private fun setupAddHobbyButton() {
        binding.addHobbyButton.setOnClickListener {
            showAddHobbyDialog()
        }
    }

    private fun showAddHobbyDialog() {
        val addHobbyDialog = AddHobbyDialogFragment.newInstance { newHobby ->
            hobbiesAdapter.addHobby(newHobby) // Add the new hobby to the RecyclerView
        }
        addHobbyDialog.show(supportFragmentManager, AddHobbyDialogFragment.TAG)
    }
}
