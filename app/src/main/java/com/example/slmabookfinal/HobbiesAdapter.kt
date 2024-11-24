package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemHobbyBinding

class HobbiesAdapter(
    private var hobbies: MutableList<Hobby>,
    private val isSelectable: Boolean = false
) : RecyclerView.Adapter<HobbiesAdapter.HobbyViewHolder>() {

    private val selectedHobbies = mutableListOf<Hobby>()

    inner class HobbyViewHolder(private val binding: ItemHobbyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hobby: Hobby) {
            binding.hobbyIcon.setImageResource(hobby.iconResId)
            binding.hobbyName.text = hobby.name

            // Update the background based on selection
            if (selectedHobbies.contains(hobby)) {
                binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_selected_background)
            } else {
                binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_background)
            }

            // Handle selection logic if enabled
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

    fun getSelectedHobbies(): List<Hobby> = selectedHobbies

    fun addHobby(hobby: Hobby) {
        hobbies.add(hobby)
        notifyItemInserted(hobbies.size - 1)
    }

    fun removeHobby(hobby: Hobby) {
        val position = hobbies.indexOf(hobby)
        if (position >= 0) {
            hobbies.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateHobbies(newHobbies: List<Hobby>) {
        hobbies.clear()
        hobbies.addAll(newHobbies)
        notifyDataSetChanged()
    }

    fun getCurrentHobbies(): List<Hobby> = hobbies
}
