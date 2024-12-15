        package com.example.slmabookfinal

        import android.view.LayoutInflater
        import android.view.View
        import android.view.ViewGroup
        import androidx.recyclerview.widget.RecyclerView
        import com.example.slmabookfinal.databinding.ItemHobbyBinding


        class HobbiesAdapter(
            private var hobbies: MutableList<Hobby>,
            private val showRemoveButton: Boolean = false, // Flag to control Remove button visibility
            private val itemSpacing: Int = 16 // Default spacing for items (16dp)
        ) : RecyclerView.Adapter<HobbiesAdapter.HobbyViewHolder>() {

            private var swipedPosition: Int? = null // Track the swiped position

            inner class HobbyViewHolder(private val binding: ItemHobbyBinding) :
                RecyclerView.ViewHolder(binding.root) {

                fun bind(hobby: Hobby, isSwiped: Boolean) {
                    binding.hobbyIcon.setImageResource(hobby.iconResId)
                    binding.hobbyName.text = hobby.name

                    // No selection logic anymore, so we just keep the default background
                    binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_background)

                    // Add margin (spacing) between items
                    val layoutParams = binding.hobbyContainer.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(0, itemSpacing, 0, itemSpacing) // Top and Bottom spacing
                    binding.hobbyContainer.layoutParams = layoutParams

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

                    // Show delete button only when the item is swiped
                    binding.removeHobbyButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
                val binding = ItemHobbyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HobbyViewHolder(binding)
            }

            override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
                val hobby = hobbies[position]
                val isSwiped = swipedPosition == position // Check if this item is swiped
                holder.bind(hobby, isSwiped)
            }

            override fun getItemCount(): Int = hobbies.size

            // Add a new hobby and notify the adapter
            fun addHobby(hobby: Hobby) {
                if (!hobbies.contains(hobby)) {
                    hobbies.add(hobby)
                    notifyItemInserted(hobbies.size - 1)
                }
                swipedPosition = null // Reset swipe position when a new item is added
            }

            // Remove a hobby and notify the adapter
            fun removeHobby(hobby: Hobby) {
                val position = hobbies.indexOf(hobby)
                if (position >= 0) {
                    hobbies.removeAt(position)
                    if (swipedPosition == position) {
                        swipedPosition = null // Reset swipe state after deletion
                    }
                    notifyItemRemoved(position)
                }
            }

            // Update the hobbies list and notify the adapter
            fun updateHobbies(newHobbies: List<Hobby>) {
                hobbies.clear()
                hobbies.addAll(newHobbies)
                notifyDataSetChanged()
                swipedPosition = null // Reset swipe position when the list is updated
            }

            // Returns the current list of hobbies
            fun getCurrentHobbies(): List<Hobby> = hobbies

            // Function to handle swipe and remove item
            fun removeItem(position: Int) {
                if (position >= 0 && position < hobbies.size) {
                    hobbies.removeAt(position)
                    if (swipedPosition == position) {
                        swipedPosition = null // Reset swipe state after deletion
                    }
                    notifyItemRemoved(position)
                }
            }

            // Function to toggle the swiped state
            fun toggleSwipe(position: Int) {
                val previousSwipedPosition = swipedPosition
                swipedPosition = if (swipedPosition == position) null else position // Toggle swipe state
                if (previousSwipedPosition != null) {
                    notifyItemChanged(previousSwipedPosition) // Reset previous swiped item
                }
                notifyItemChanged(position) // Update newly swiped item
            }
        }
