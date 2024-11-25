package com.example.slmabookfinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class AvatarSelectionActivity : AppCompatActivity() {

    private val avatarList = listOf(
        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
        R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_selection)

        val gridView = findViewById<GridView>(R.id.avatarGridView)
        val adapter = AvatarAdapter(this, avatarList)
        gridView.adapter = adapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedAvatar = avatarList[position]
            val resultIntent = Intent().apply {
                putExtra("selectedAvatar", selectedAvatar)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
