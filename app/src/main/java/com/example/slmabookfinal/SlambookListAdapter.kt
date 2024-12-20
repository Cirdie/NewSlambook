package com.example.slmabookfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SlambookListAdapter(
    private val slambooks: MutableList<SlambookEntry>,
    private val onItemClick: (SlambookEntry) -> Unit,
    private val onRemoveClick: (SlambookEntry) -> Unit
) : RecyclerView.Adapter<SlambookListAdapter.SlambookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlambookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slambook, parent, false)
        return SlambookViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlambookViewHolder, position: Int) {
        val slambook = slambooks[position]
        holder.bind(slambook, onItemClick, onRemoveClick)
    }

    override fun getItemCount(): Int = slambooks.size

    fun removeSlambook(slambook: SlambookEntry) {
        val position = slambooks.indexOf(slambook)
        if (position != -1) {
            slambooks.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, slambooks.size)
        }
    }


    class SlambookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        private val slambookNameTextView: TextView = itemView.findViewById(R.id.slambookNameTextView)
        private val taglineTextView: TextView = itemView.findViewById(R.id.taglineTextView)
        private val removeButton: ImageView = itemView.findViewById(R.id.removeButton)

        fun bind(
            slambook: SlambookEntry,
            onItemClick: (SlambookEntry) -> Unit,
            onRemoveClick: (SlambookEntry) -> Unit
        ) {
            avatarImageView.setImageResource(slambook.avatarId)

            slambookNameTextView.text = slambook.slambookName
            taglineTextView.text = slambook.slambookTagline

            itemView.setOnClickListener { onItemClick(slambook) }

            removeButton.setOnClickListener { onRemoveClick(slambook) }
        }
    }
}
