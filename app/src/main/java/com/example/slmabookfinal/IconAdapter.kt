package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ItemIconBinding

class IconAdapter(
    private val icons: List<Int>, // List of drawable resource IDs
    private val onIconSelected: (Int) -> Unit // Callback for selected icon
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    inner class IconViewHolder(private val binding: ItemIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(iconResId: Int) {
            binding.iconImageView.setImageResource(iconResId) // Set the icon image

            binding.iconContainer.setOnClickListener {
                onIconSelected(iconResId) // Trigger callback when an icon is selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int = icons.size
}
