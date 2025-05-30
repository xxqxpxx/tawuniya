package com.example.tawuniya.data.repository


import com.example.tawuniya.data.local.LikedUsersDataStore
import com.example.tawuniya.data.remote.ApiService
import com.example.tawuniya.data.remote.dto.toUser
import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val likedUsersDataStore: LikedUsersDataStore
) : UserRepository {
    override suspend fun getUsers(): Flow<List<User>> {
        return try {
            val apiUsers = withContext(Dispatchers.IO) {
                apiService.getUsers().map { it.toUser() }
            }
            
            likedUsersDataStore.getLikedUsers().map { likedUsers ->
                apiUsers.map { user ->
                    user.copy(isLiked = likedUsers.any { it.id == user.id })
                }
            }
        } catch (e: Exception) {
            likedUsersDataStore.getLikedUsers().map { likedUsers ->
                likedUsers.map { user ->
                    user.copy(isLiked = true)
                }
            }
        }
    }

    override suspend fun saveLikedUser(user: User) {
        likedUsersDataStore.saveLikedUser(user)
    }

    override suspend fun removeLikedUser(user: User) {
        likedUsersDataStore.removeLikedUser(user)
    }

    override fun getLikedUsers(): Flow<List<User>> {
        return likedUsersDataStore.getLikedUsers()
    }
}