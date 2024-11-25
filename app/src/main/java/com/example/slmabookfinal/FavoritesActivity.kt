package com.example.slmabookfinal

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.slambookfinal.Favorite
import com.example.slmabookfinal.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

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
    }

    private fun showAddFavoriteDialog(favorite: Favorite, iconResId: Int, textView: TextView) {
        val dialog = AddFavoriteDialogFragment(
            favorite = favorite,
            iconResId = iconResId
        ) { updatedFavorite ->
            // Update the corresponding TextView when an item is added
            textView.text = updatedFavorite.items.joinToString("\n") { "• $it" }
            textView.visibility = View.VISIBLE // Show the list if it was hidden
        }
        dialog.show(supportFragmentManager, "AddFavoriteDialog")
    }
}
