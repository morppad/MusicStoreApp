package com.example.musicstoreapp.data

import java.sql.ResultSet

class ProductRepository {
    fun getProducts(): List<String> {
        val products = mutableListOf<String>()
        val connection = DatabaseConnection.getConnection()

        if (connection != null) {
            val query = "SELECT name FROM products" // Ваш SQL-запрос
            try {
                val statement = connection.createStatement()
                val resultSet: ResultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    products.add(resultSet.getString("name"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.close()
            }
        }

        return products
    }
}
