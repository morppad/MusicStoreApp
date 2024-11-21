package com.example.musicstoreapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicstoreapp.databinding.ActivityMainBinding
import com.example.musicstoreapp.ui.ProductActivity
import com.example.musicstoreapp.ui.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Переход на экран списка продуктов
        binding.btnViewProducts.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }

        // Переход на экран регистрации пользователей
        binding.btnRegisterUser.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Выход из приложения
        binding.btnExit.setOnClickListener {
            finish()
        }
    }
}
