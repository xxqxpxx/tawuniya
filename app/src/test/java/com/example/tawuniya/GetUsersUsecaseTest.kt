package com.example.tawuniya

import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.repos.UserRepository
import com.example.tawuniya.domain.usecase.GetLikedUsersUseCase
import com.example.tawuniya.domain.usecase.GetUsersUseCase
import com.example.tawuniya.domain.usecase.SaveLikedUserUseCase

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setUp() {

        userRepository = mockk()

        getUsersUseCase = GetUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return list of users from repository`() = runTest {

        val expectedUsers = listOf(
            User(1, "User 1", "user1", "user1@example.com", "123", "website1.com"),
            User(2, "User 2", "user2", "user2@example.com", "456", "website2.com")
        )
        coEvery { userRepository.getUsers() } returns expectedUsers

        val actualUsers = getUsersUseCase()

        assertEquals(expectedUsers, actualUsers)
    }
}
