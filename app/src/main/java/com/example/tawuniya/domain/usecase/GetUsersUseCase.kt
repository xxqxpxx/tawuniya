package com.example.tawuniya.domain.usecase


import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return userRepository.getUsers()
    }
}