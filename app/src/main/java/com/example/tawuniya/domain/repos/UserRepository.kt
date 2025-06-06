package com.example.tawuniya.domain.repos

import com.example.tawuniya.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<List<User>>
    suspend fun saveLikedUser(user: User)
    suspend fun removeLikedUser(user: User)
    fun getLikedUsers(): Flow<List<User>>
}