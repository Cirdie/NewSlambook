package com.example.slambookfinal

data class Favorite(
    val category: String, // Category: e.g., Movies, Music, etc.
    val items: MutableList<String> = mutableListOf() // List of items within the category
)
