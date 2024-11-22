package com.example.musicstoreapp.model

data class User(
    val userId: String? = null, // Генерируемое сервером
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val role: String, // Роль по умолчанию
    val createdAt: String? = null, // Генерируется на сервере
    val password: String
)
