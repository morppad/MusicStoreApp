package com.example.musicstoreapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicstoreapp.adapter.ProductAdapter
import com.example.musicstoreapp.data.DatabaseConnection
import com.example.musicstoreapp.databinding.ActivityProductBinding
import com.example.musicstoreapp.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = productAdapter
        }
    }

    private fun fetchProducts() {
        GlobalScope.launch(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val query = "SELECT * FROM products"
            val products = mutableListOf<Product>()
            try {
                val statement = connection?.createStatement()
                val resultSet = statement?.executeQuery(query)
                while (resultSet?.next() == true) {
                    val id = resultSet.getInt("id") // Убедитесь, что поля совпадают с базой
                    val name = resultSet.getString("name")
                    val price = resultSet.getDouble("price")
                    val product = Product(id = id, name = name, price = price)
                    products.add(product)
                }
                withContext(Dispatchers.Main) {
                    productAdapter.submitList(products)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductActivity, "Error fetching products", Toast.LENGTH_SHORT).show()
                }
            } finally {
                connection?.close()
            }
        }
    }
}
