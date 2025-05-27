package com.example.tawuniya.domain.usecase

import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import javax.inject.Inject

class SaveLikedUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.saveLikedUser(user)
    }
}