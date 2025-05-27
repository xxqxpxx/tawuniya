

package com.example.tawuniya

import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import com.example.tawuniya.domain.usecase.GetLikedUsersUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class GetLikedUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getLikedUsersUseCase: GetLikedUsersUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        getLikedUsersUseCase = GetLikedUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return flow of liked users from repository`() = runTest {
         val expectedLikedUsers = listOf(
            User(1, "Liked User 1", "liked1", "liked1@example.com", "789", "liked1.com")
        )
        every { userRepository.getLikedUsers() } returns flowOf(expectedLikedUsers)

         val actualLikedUsers = getLikedUsersUseCase().first()

         assertEquals(expectedLikedUsers, actualLikedUsers)
    }
}
