package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.slmabookfinal.databinding.ActivityFavoritesBinding
import com.example.slmabookfinal.utils.ProgressDialog

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slambookEntry: SlambookEntry

    private val favorites = mutableListOf(
        Favorite(category = "Movies", items = mutableListOf()),
        Favorite(category = "Music", items = mutableListOf()),
        Favorite(category = "Colors", items = mutableListOf()),
        Favorite(category = "Books", items = mutableListOf()),
        Favorite(category = "Sports", items = mutableListOf())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()

        updateCreateSlambookButtonState()

        setUpAddButtons()

        setUpRecyclerView(binding.favoriteMoviesRecyclerView, favorites[0].items)
        setUpRecyclerView(binding.favoriteMusicRecyclerView, favorites[1].items)
        setUpRecyclerView(binding.favoriteColorsRecyclerView, favorites[2].items)
        setUpRecyclerView(binding.favoriteBooksRecyclerView, favorites[3].items)
        setUpRecyclerView(binding.favoriteSportsRecyclerView, favorites[4].items)

        binding.createSlambookButton.setOnClickListener {
            saveFavoritesToSlambookEntry()
            showProgressAndComplete()
        }
    }

    private fun setUpAddButtons() {
        binding.addMovieButton.setOnClickListener {
            showAddFavoriteDialog(favorites[0], binding.favoriteMoviesRecyclerView, R.drawable.ic_movies)
        }
        binding.addMusicButton.setOnClickListener {
            showAddFavoriteDialog(favorites[1], binding.favoriteMusicRecyclerView, R.drawable.ic_music)
        }
        binding.addColorButton.setOnClickListener {
            showAddFavoriteDialog(favorites[2], binding.favoriteColorsRecyclerView, R.drawable.ic_colors)
        }
        binding.addBookButton.setOnClickListener {
            showAddFavoriteDialog(favorites[3], binding.favoriteBooksRecyclerView, R.drawable.ic_books)
        }
        binding.addSportButton.setOnClickListener {
            showAddFavoriteDialog(favorites[4], binding.favoriteSportsRecyclerView, R.drawable.ic_sports)
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, items: MutableList<String>) {
        val adapter = FavoritesAdapter(items) { item ->
            items.remove(item)
            recyclerView.adapter?.notifyDataSetChanged() // Reset the RecyclerView UI
            updateCreateSlambookButtonState()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                (recyclerView.adapter as FavoritesAdapter).toggleSwipe(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showAddFavoriteDialog(favorite: Favorite, recyclerView: RecyclerView, iconResId: Int) {
        val dialog = AddFavoriteDialogFragment(favorite, iconResId) { updatedFavorite ->
            recyclerView.adapter = FavoritesAdapter(updatedFavorite.items) { item ->
                updatedFavorite.items.remove(item)
                recyclerView.adapter?.notifyDataSetChanged()
                updateCreateSlambookButtonState()
            }
            updateCreateSlambookButtonState()
        }
        dialog.show(supportFragmentManager, "AddFavoriteDialog")
    }

    private fun updateCreateSlambookButtonState() {
        val hasFavorites = favorites.any { it.items.isNotEmpty() }
        binding.createSlambookButton.apply {
            isEnabled = hasFavorites
            alpha = if (hasFavorites) 1f else 0.5f
        }
    }

    private fun saveFavoritesToSlambookEntry() {
        slambookEntry.favorites = favorites
    }

    private fun showProgressAndComplete() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Creating your Slambook...")

        binding.createSlambookButton.isEnabled = false
        binding.createSlambookButton.alpha = 0.5f

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()

            SlambookStorage.addSlambook(slambookEntry)

            proceedToChooseActivity()
        }, 2000)
    }

    private fun proceedToChooseActivity() {
        val intent = Intent(this, ChooseActivity::class.java)
        startActivity(intent)
        finish()
    }
}
