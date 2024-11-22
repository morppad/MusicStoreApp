package com.example.musicstoreapp.repository

import com.example.musicstoreapp.model.User
import com.example.musicstoreapp.network.ApiClient
import com.example.musicstoreapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    fun registerUser(user: User, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val call = apiService.registerUser(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Registration failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onError("Network error: ${t.localizedMessage}")
            }
        })
    }
}
