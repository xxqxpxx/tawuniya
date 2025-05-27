package com.example.tawuniya.data.repository


import com.example.tawuniya.data.local.LikedUsersDataStore
import com.example.tawuniya.data.remote.ApiService
import com.example.tawuniya.data.remote.dto.toUser
import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val likedUsersDataStore: LikedUsersDataStore
) : UserRepository {
    override suspend fun getUsers(): Flow<List<User>> {
        return flowOf(apiService.getUsers().map { it.toUser() })
    }

    override suspend fun saveLikedUser(user: User) {
        likedUsersDataStore.saveLikedUser(user)
    }

    override fun getLikedUsers(): Flow<List<User>> {
        return likedUsersDataStore.getLikedUsers()
    }
}