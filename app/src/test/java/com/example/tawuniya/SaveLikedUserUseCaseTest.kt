package com.example.tawuniya


import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import com.example.tawuniya.domain.usecase.SaveLikedUserUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveLikedUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var saveLikedUserUseCase: SaveLikedUserUseCase

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)

        saveLikedUserUseCase = SaveLikedUserUseCase(userRepository)
    }

    @Test
    fun `invoke should call saveLikedUser on repository`() = runTest {

        val userToSave = User(1, "Test User", "testuser", "test@example.com", "111", "test.com")


        saveLikedUserUseCase(userToSave)


        coVerify(exactly = 1) { userRepository.saveLikedUser(userToSave) }
    }
}