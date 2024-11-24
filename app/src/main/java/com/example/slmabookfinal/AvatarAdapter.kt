package com.example.slmabookfinal.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView

class AvatarAdapter(private val context: Context, private val avatars: List<Int>) : BaseAdapter() {

    override fun getCount(): Int {
        return avatars.size
    }

    override fun getItem(position: Int): Int {
        return avatars[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)
            imageView.layoutParams = AbsListView.LayoutParams(200, 200) // Set avatar size
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8)
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(avatars[position])
        return imageView
    }
}
