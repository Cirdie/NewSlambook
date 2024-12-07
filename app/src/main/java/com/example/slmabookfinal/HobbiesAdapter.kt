package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemHobbyBinding

class HobbiesAdapter(
    private var hobbies: MutableList<Hobby>,
    private val isSelectable: Boolean = false,
    private val showRemoveButton: Boolean = false // Flag to control Remove button visibility
) : RecyclerView.Adapter<HobbiesAdapter.HobbyViewHolder>() {

    private val selectedHobbies = mutableListOf<Hobby>()

    inner class HobbyViewHolder(private val binding: ItemHobbyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hobby: Hobby) {
            binding.hobbyIcon.setImageResource(hobby.iconResId)
            binding.hobbyName.text = hobby.name

            // Update the background based on selection
            binding.hobbyContainer.setBackgroundResource(
                if (selectedHobbies.contains(hobby))
                    R.drawable.hobby_item_selected_background
                else
                    R.drawable.hobby_item_background
            )

            // Handle selection logic if selectable
            if (isSelectable) {
                binding.hobbyContainer.setOnClickListener {
                    if (selectedHobbies.contains(hobby)) {
                        selectedHobbies.remove(hobby)
                        binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_background)
                    } else {
                        selectedHobbies.add(hobby)
                        binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_selected_background)
                    }
                }
            } else {
                // If not selectable, ensure no interaction
                binding.hobbyContainer.setOnClickListener(null)
            }

            // Show or hide the Remove button based on the flag
            if (showRemoveButton) {
                binding.removeHobbyButton.visibility = View.VISIBLE
                binding.removeHobbyButton.setOnClickListener {
                    // Remove the hobby when clicked
                    removeHobby(hobby)
                }
            } else {
                binding.removeHobbyButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
        val binding = ItemHobbyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HobbyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
        holder.bind(hobbies[position])
    }

    override fun getItemCount(): Int = hobbies.size

    // Returns the list of selected hobbies
    fun getSelectedHobbies(): List<Hobby> = selectedHobbies

    // Add a new hobby and notify the adapter
    fun addHobby(hobby: Hobby) {
        if (!hobbies.contains(hobby)) {
            hobbies.add(hobby)
            notifyItemInserted(hobbies.size - 1)
        }
    }

    // Remove a hobby and notify the adapter
    fun removeHobby(hobby: Hobby) {
        val position = hobbies.indexOf(hobby)
        if (position >= 0) {
            hobbies.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Update the hobbies list and notify the adapter
    fun updateHobbies(newHobbies: List<Hobby>) {
        hobbies.clear()
        hobbies.addAll(newHobbies)
        notifyDataSetChanged()
    }

    // Returns the current list of hobbies
    fun getCurrentHobbies(): List<Hobby> = hobbies
}
