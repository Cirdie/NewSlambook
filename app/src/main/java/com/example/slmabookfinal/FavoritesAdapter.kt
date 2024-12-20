package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val items: MutableList<String>,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var swipedPosition: Int? = null

    inner class ViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemTextView = binding.itemTextView
        val deleteButton = binding.deleteButton

        fun bind(item: String, isSwiped: Boolean) {
            itemTextView.text = item

            deleteButton.visibility = if (isSwiped) View.VISIBLE else View.GONE

            // Set delete button click listener
            deleteButton.setOnClickListener {
                onDeleteClicked(item)
                removeItem(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isSwiped = swipedPosition == position
        holder.bind(item, isSwiped)
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            if (swipedPosition == position) {
                swipedPosition = null
            }
            notifyItemRemoved(position)
        }
    }

    // Function to toggle the swiped state
    fun toggleSwipe(position: Int) {
        val previousSwipedPosition = swipedPosition
        swipedPosition = if (swipedPosition == position) null else position
        if (previousSwipedPosition != null) {
            notifyItemChanged(previousSwipedPosition)
        }
        notifyItemChanged(position)
    }

    fun updateFavorites(newFavorites: List<String>) {
        items.clear()
        items.addAll(newFavorites)
        notifyDataSetChanged()
    }


}
