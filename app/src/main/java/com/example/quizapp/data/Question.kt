package com.example.quizapp.data

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)