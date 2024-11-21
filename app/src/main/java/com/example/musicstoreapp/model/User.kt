package com.example.musicstoreapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users") // Аннотация @Entity для таблицы Room
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Уникальный идентификатор
    val name: String,                                // Имя пользователя
    val email: String,                               // Email
    val password: String,                            // Пароль
    val phone: String,                               // Номер телефона
    val address: String,                             // Адрес
    val role: String = "customer"                   // Роль пользователя (по умолчанию покупатель)
)
