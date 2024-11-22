package com.example.musicstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicstoreapp.ui.AdminActivity
import com.example.musicstoreapp.ui.RegisterActivity
import com.example.musicstoreapp.ui.UserActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // Login button action
        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to registration screen
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build()

                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/auth/login")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Redirect to another activity
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
