package com.example.musicstoreapp.network

import com.example.musicstoreapp.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/register")
    fun registerUser(@Body user: User): Call<User>
    @FormUrlEncoded
    @POST("/api/users/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<User>

}
