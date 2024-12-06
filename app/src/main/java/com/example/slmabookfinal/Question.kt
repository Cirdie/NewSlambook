package com.example.slmabookfinal
import java.io.Serializable

data class Question(
    val questionText: String,
    val answerText: String
) : Serializable