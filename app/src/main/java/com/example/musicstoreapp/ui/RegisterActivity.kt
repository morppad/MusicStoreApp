package com.example.musicstoreapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicstoreapp.databinding.ActivityRegisterBinding
import com.example.musicstoreapp.model.User
import com.example.musicstoreapp.repository.UserRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.inputName.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val phone = binding.inputPhone.text.toString()
            val address = binding.inputAddress.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    role = "customer", // Роль по умолчанию
                    password = password
                )
                registerUser(user)
            }
        }
    }

    private fun registerUser(user: User) {
        UserRepository.registerUser(
            user,
            onSuccess = {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
