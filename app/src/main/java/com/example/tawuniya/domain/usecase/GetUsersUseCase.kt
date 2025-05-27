package com.example.tawuniya.domain.usecase


import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getUsers()
    }
}