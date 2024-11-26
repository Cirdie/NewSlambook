package com.example.slmabookfinal

data class Favorite(
    val category: String,
    val items: MutableList<String> = mutableListOf()
)
