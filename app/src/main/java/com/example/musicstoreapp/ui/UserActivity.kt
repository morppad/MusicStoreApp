package com.example.musicstoreapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicstoreapp.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

class UserActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var editPhone: EditText
    private lateinit var editAddress: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        userPhone = findViewById(R.id.userPhone)
        editPhone = findViewById(R.id.editPhone)
        editAddress = findViewById(R.id.editAddress)
        saveButton = findViewById(R.id.saveButton)

        // Load user data from the backend
        loadUserData()

        saveButton.setOnClickListener {
            val newPhone = editPhone.text.toString()
            val newAddress = editAddress.text.toString()

            if (newPhone.isEmpty() || newAddress.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                updateUserDetails(newPhone, newAddress)
            }
        }
    }

    private fun loadUserData() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/user/details")
                    .build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val json = JSONObject(response.body?.string() ?: "{}")
                    val name = json.getString("name")
                    val email = json.getString("email")
                    val phone = json.getString("phone")

                    runOnUiThread {
                        userName.text = name
                        userEmail.text = email
                        userPhone.text = phone
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUserDetails(newPhone: String, newAddress: String) {
        thread {
            try {
                val client = OkHttpClient()
                val json = JSONObject().apply {
                    put("phone", newPhone)
                    put("address", newAddress)
                }
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    json.toString()
                )
                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/user/update")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to update user details", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Network error while updating details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
