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
        R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
        R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
        R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12,
        R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15,
        R.drawable.avatar16, R.drawable.avatar17, R.drawable.avatar18,
        R.drawable.avatar19, R.drawable.avatar20, R.drawable.avatar21,
        R.drawable.avatar22, R.drawable.avatar23, R.drawable.avatar24,
        R.drawable.avatar25, R.drawable.avatar26, R.drawable.avatar27,
        R.drawable.avatar28, R.drawable.avatar29, R.drawable.avatar30,
        R.drawable.avatar31, R.drawable.avatar32, R.drawable.avatar33,
        R.drawable.avatar34, R.drawable.avatar35, R.drawable.avatar36,
        R.drawable.avatar37, R.drawable.avatar38, R.drawable.avatar39,
        R.drawable.avatar40
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
