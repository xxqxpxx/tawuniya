package com.example.tawuniya.domain.usecase


import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLikedUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getLikedUsers()
    }
}
