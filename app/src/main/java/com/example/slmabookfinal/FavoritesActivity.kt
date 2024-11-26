package com.example.slmabookfinal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityFavoritesBinding
import com.example.slmabookfinal.utils.ProgressDialog

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var progressDialog: ProgressDialog

    private val favorites = mutableListOf(
        Favorite(category = "Movies"),
        Favorite(category = "Music"),
        Favorite(category = "Colors"),
        Favorite(category = "Books"),
        Favorite(category = "Sports")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)

        // Initialize Create Slambook button visibility
        updateCreateSlambookButtonState()

        // Set up Add button listeners
        binding.addMovieButton.setOnClickListener {
            showAddFavoriteDialog(favorites[0], R.drawable.ic_movies, binding.favoriteMoviesList)
        }
        binding.addMusicButton.setOnClickListener {
            showAddFavoriteDialog(favorites[1], R.drawable.ic_music, binding.favoriteMusicList)
        }
        binding.addColorButton.setOnClickListener {
            showAddFavoriteDialog(favorites[2], R.drawable.ic_colors, binding.favoriteColorsList)
        }
        binding.addBooksButton.setOnClickListener {
            showAddFavoriteDialog(favorites[3], R.drawable.ic_books, binding.favoriteBooksList)
        }
        binding.addSportsButton.setOnClickListener {
            showAddFavoriteDialog(favorites[4], R.drawable.ic_sports, binding.favoriteSportsList)
        }

        // Handle Create Slambook button click
        binding.createSlambookButton.setOnClickListener {
            showProgressAndComplete()
        }
    }

    private fun showAddFavoriteDialog(favorite: Favorite, iconResId: Int, textView: TextView) {
        val dialog = AddFavoriteDialogFragment(
            favorite = favorite,
            iconResId = iconResId
        ) { updatedFavorite ->
            // Update the corresponding TextView when an item is added
            textView.text = updatedFavorite.items.joinToString("\n") { "• $it" }
            textView.visibility = View.VISIBLE // Show the list if it was hidden
            updateCreateSlambookButtonState() // Update the Create Slambook button state
        }
        dialog.show(supportFragmentManager, "AddFavoriteDialog")
    }

    private fun updateCreateSlambookButtonState() {
        // Check if at least one favorite has items
        val hasFavorites = favorites.any { it.items.isNotEmpty() }
        binding.createSlambookButton.apply {
            isEnabled = hasFavorites
            alpha = if (hasFavorites) 1f else 0.5f // Adjust transparency
        }
    }

    private fun showProgressAndComplete() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Creating your Slambook...")

        binding.createSlambookButton.isEnabled = false // Prevent multiple clicks
        binding.createSlambookButton.alpha = 0.5f

        // Simulate a delay for the creation process
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            // Proceed to the next activity or finalize the process
            finish()
        }, 2000) // 2-second delay
    }
}
