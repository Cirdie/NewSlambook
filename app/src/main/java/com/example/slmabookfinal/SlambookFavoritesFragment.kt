package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slmabookfinal.databinding.FragmentSlambookFavoritesBinding

class SlambookFavoritesFragment : Fragment() {

    private lateinit var binding: FragmentSlambookFavoritesBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // Shared ViewModel
    private lateinit var favoritesAdapters: Map<String, FavoritesAdapter> // Adapters for each category

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSlambookFavoritesBinding.inflate(inflater, container, false)

        setupAdapters()

        sharedViewModel.slambookData.observe(viewLifecycleOwner, Observer { slambook ->
            if (slambook == null) {
                displayEmptyState()
            } else {
                displayFavorites(slambook)
            }
        })

        return binding.root
    }

    private fun setupAdapters() {
        favoritesAdapters = mapOf(
            "Movies" to FavoritesAdapter(mutableListOf()) { item -> removeFavorite("Movies", item) },
            "Music" to FavoritesAdapter(mutableListOf()) { item -> removeFavorite("Music", item) },
            "Colors" to FavoritesAdapter(mutableListOf()) { item -> removeFavorite("Colors", item) },
            "Books" to FavoritesAdapter(mutableListOf()) { item -> removeFavorite("Books", item) },
            "Sports" to FavoritesAdapter(mutableListOf()) { item -> removeFavorite("Sports", item) }
        )

        binding.favoriteMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapters["Movies"]
        }

        binding.favoriteMusicRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapters["Music"]
        }

        binding.favoriteColorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapters["Colors"]
        }

        binding.favoriteBooksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapters["Books"]
        }

        binding.favoriteSportsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapters["Sports"]
        }
    }

    private fun displayEmptyState() {
        binding.titleText.text = "No favorites selected yet"
        binding.subtitleText.visibility = View.GONE
        // Hide all category containers if empty
        hideCategoryContainers()
    }

    private fun displayFavorites(slambook: SlambookEntry) {
        binding.titleText.text = "Your Favorite Things"
        binding.subtitleText.visibility = View.VISIBLE

        val movieFavorites = slambook.favorites.find { it.category == "Movies" }?.items ?: listOf()
        val musicFavorites = slambook.favorites.find { it.category == "Music" }?.items ?: listOf()
        val colorFavorites = slambook.favorites.find { it.category == "Colors" }?.items ?: listOf()
        val bookFavorites = slambook.favorites.find { it.category == "Books" }?.items ?: listOf()
        val sportsFavorites = slambook.favorites.find { it.category == "Sports" }?.items ?: listOf()

        favoritesAdapters["Movies"]?.updateFavorites(movieFavorites)
        favoritesAdapters["Music"]?.updateFavorites(musicFavorites)
        favoritesAdapters["Colors"]?.updateFavorites(colorFavorites)
        favoritesAdapters["Books"]?.updateFavorites(bookFavorites)
        favoritesAdapters["Sports"]?.updateFavorites(sportsFavorites)

        // Update the visibility of each category based on whether there are items
        toggleCategoryVisibility("Movies", movieFavorites)
        toggleCategoryVisibility("Music", musicFavorites)
        toggleCategoryVisibility("Colors", colorFavorites)
        toggleCategoryVisibility("Books", bookFavorites)
        toggleCategoryVisibility("Sports", sportsFavorites)
    }

    private fun toggleCategoryVisibility(category: String, items: List<String>) {
        val containerView = when (category) {
            "Movies" -> binding.favoriteMoviesContainer
            "Music" -> binding.favoriteMusicContainer
            "Colors" -> binding.favoriteColorsContainer
            "Books" -> binding.favoriteBooksContainer
            "Sports" -> binding.favoriteSportsContainer
            else -> null
        }

        containerView?.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun removeFavorite(category: String, item: String) {
        val favoriteCategory = sharedViewModel.slambookData.value?.favorites?.find { it.category == category }
        favoriteCategory?.items?.remove(item)
        sharedViewModel.slambookData.value = sharedViewModel.slambookData.value // Trigger LiveData update
    }

    // Helper method to hide all categories (when there are no favorites)
    private fun hideCategoryContainers() {
        binding.favoriteMoviesContainer.visibility = View.GONE
        binding.favoriteMusicContainer.visibility = View.GONE
        binding.favoriteColorsContainer.visibility = View.GONE
        binding.favoriteBooksContainer.visibility = View.GONE
        binding.favoriteSportsContainer.visibility = View.GONE
    }
}
