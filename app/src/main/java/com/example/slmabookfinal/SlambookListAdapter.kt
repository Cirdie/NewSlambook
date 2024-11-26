package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SlambookListAdapter(
    private val slambooks: List<SlambookEntry>, // Ensure this list is populated correctly
    private val onItemClick: (SlambookEntry) -> Unit // Callback for item clicks
) : RecyclerView.Adapter<SlambookListAdapter.SlambookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlambookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slambook, parent, false)
        return SlambookViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlambookViewHolder, position: Int) {
        val slambook = slambooks[position]
        holder.bind(slambook, onItemClick)
    }

    override fun getItemCount(): Int = slambooks.size

    class SlambookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        private val slambookNameTextView: TextView = itemView.findViewById(R.id.slambookNameTextView)
        private val taglineTextView: TextView = itemView.findViewById(R.id.taglineTextView)

        fun bind(slambook: SlambookEntry, onItemClick: (SlambookEntry) -> Unit) {
            avatarImageView.setImageResource(slambook.avatarId) // Set avatar
            slambookNameTextView.text = slambook.slambookName // Display Slambook name
            taglineTextView.text = slambook.slambookTagline // Display tagline

            // Attach click listener
            itemView.setOnClickListener { onItemClick(slambook) }
        }
    }
}
