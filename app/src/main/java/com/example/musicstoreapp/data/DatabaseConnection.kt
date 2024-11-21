package com.example.musicstoreapp.data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnection {
    private const val URL = "jdbc:postgresql://213.108.23.147:5432/postgres" // Укажите ваш сервер, порт и имя базы данных
    private const val USER = "postgres" // Имя пользователя базы данных
    private const val PASSWORD = "BtdA2653" // Пароль пользователя базы данных

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
