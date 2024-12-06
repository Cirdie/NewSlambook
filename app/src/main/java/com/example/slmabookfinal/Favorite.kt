package com.example.slmabookfinal
import java.io.Serializable

data class Favorite(
    val category: String,
    val items: MutableList<String> = mutableListOf()
) : Serializable

