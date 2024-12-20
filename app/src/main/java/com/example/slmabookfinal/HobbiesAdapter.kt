        package com.example.slmabookfinal

        import android.view.LayoutInflater
        import android.view.View
        import android.view.ViewGroup
        import androidx.recyclerview.widget.RecyclerView
        import com.example.slmabookfinal.databinding.ItemHobbyBinding


        class HobbiesAdapter(
            private var hobbies: MutableList<Hobby>,
            private val showRemoveButton: Boolean = false,
            private val itemSpacing: Int = 16
        ) : RecyclerView.Adapter<HobbiesAdapter.HobbyViewHolder>() {

            private var swipedPosition: Int? = null

            inner class HobbyViewHolder(private val binding: ItemHobbyBinding) :
                RecyclerView.ViewHolder(binding.root) {

                fun bind(hobby: Hobby, isSwiped: Boolean) {
                    binding.hobbyIcon.setImageResource(hobby.iconResId)
                    binding.hobbyName.text = hobby.name

                    binding.hobbyContainer.setBackgroundResource(R.drawable.hobby_item_background)

                    val layoutParams = binding.hobbyContainer.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(0, itemSpacing, 0, itemSpacing) // Top and Bottom spacing
                    binding.hobbyContainer.layoutParams = layoutParams

                    if (showRemoveButton) {
                        binding.removeHobbyButton.visibility = View.VISIBLE
                        binding.removeHobbyButton.setOnClickListener {
                            removeHobby(hobby)
                        }
                    } else {
                        binding.removeHobbyButton.visibility = View.GONE
                    }

                    binding.removeHobbyButton.visibility = if (isSwiped) View.VISIBLE else View.GONE
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
                val binding = ItemHobbyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HobbyViewHolder(binding)
            }

            override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
                val hobby = hobbies[position]
                val isSwiped = swipedPosition == position
                holder.bind(hobby, isSwiped)
            }

            override fun getItemCount(): Int = hobbies.size

            fun addHobby(hobby: Hobby) {
                if (!hobbies.contains(hobby)) {
                    hobbies.add(hobby)
                    notifyItemInserted(hobbies.size - 1)
                }
                swipedPosition = null
            }

            fun removeHobby(hobby: Hobby) {
                val position = hobbies.indexOf(hobby)
                if (position >= 0) {
                    hobbies.removeAt(position)
                    if (swipedPosition == position) {
                        swipedPosition = null
                    }
                    notifyItemRemoved(position)
                }
            }

            fun updateHobbies(newHobbies: List<Hobby>) {
                hobbies.clear()
                hobbies.addAll(newHobbies)
                notifyDataSetChanged()
                swipedPosition = null
            }

            fun getCurrentHobbies(): List<Hobby> = hobbies

            fun removeItem(position: Int) {
                if (position >= 0 && position < hobbies.size) {
                    hobbies.removeAt(position)
                    if (swipedPosition == position) {
                        swipedPosition = null
                    }
                    notifyItemRemoved(position)
                }
            }

            fun toggleSwipe(position: Int) {
                val previousSwipedPosition = swipedPosition
                swipedPosition = if (swipedPosition == position) null else position
                if (previousSwipedPosition != null) {
                    notifyItemChanged(previousSwipedPosition)
                }
                notifyItemChanged(position)
            }
        }
