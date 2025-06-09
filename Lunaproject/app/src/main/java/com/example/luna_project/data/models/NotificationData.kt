package com.example.luna_project.data.models


data class NotificationData(
    val id: Int,
    val title: String,
    val message: String = "E aí, Gostou? Responda com uma nota de 1 a 5 ⭐ para nos ajudar a melhorar!",
    val rating: Int? = null
)
