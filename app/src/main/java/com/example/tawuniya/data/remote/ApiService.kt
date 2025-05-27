package com.example.tawuniya.data.remote

import com.example.tawuniya.data.remote.dto.UserDto
import retrofit2.http.GET

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): List<UserDto>
}