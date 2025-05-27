
package com.example.tawuniya

import com.example.tawuniya.data.local.LikedUsersDataStore
import com.example.tawuniya.data.remote.ApiService
import com.example.tawuniya.data.remote.dto.AddressDto
import com.example.tawuniya.data.remote.dto.CompanyDto
import com.example.tawuniya.data.remote.dto.GeoDto
import com.example.tawuniya.data.remote.dto.UserDto
import com.example.tawuniya.data.remote.dto.toUser
import com.example.tawuniya.data.repository.UserRepositoryImpl
import com.example.tawuniya.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
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
class UserRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var likedUsersDataStore: LikedUsersDataStore
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @Before
    fun setUp() {

        apiService = mockk()
        likedUsersDataStore = mockk(relaxed = true)

        userRepositoryImpl = UserRepositoryImpl(apiService, likedUsersDataStore)
    }

    @Test
    fun `getUsers should fetch from API and map to domain users`() = runTest {
        // إعداد DTOs وهمية من API
        val userDtoList = listOf(
            UserDto(
                id = 1,
                name = "Leanne Graham",
                username = "Bret",
                email = "Sincere@april.biz",
                address = AddressDto("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", GeoDto("-37.3159", "81.1496")),
                phone = "1-770-736-8031 x56442",
                website = "hildegard.org",
                company = CompanyDto("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets")
            )
        )
        // إعداد المستخدمين المتوقعين بعد التحويل
        val expectedUsers = listOf(userDtoList[0].toUser())

        // إعداد سلوك ApiService المحاكي
        coEvery { apiService.getUsers() } returns userDtoList

        // استدعاء دالة getUsers من المستودع
        val actualUsers = userRepositoryImpl.getUsers()

        // التحقق من أن المستخدمين المرجعين هم المستخدمون المحولون بشكل صحيح
        assertEquals(expectedUsers, actualUsers)
        // التحقق من أن دالة getUsers تم استدعاؤها على ApiService
        coVerify(exactly = 1) { apiService.getUsers() }
    }

    @Test
    fun `saveLikedUser should call saveLikedUser on data store`() = runTest {

        val userToSave = User(1, "Test User", "testuser", "test@example.com", "111", "test.com")


        userRepositoryImpl.saveLikedUser(userToSave)


        coVerify(exactly = 1) { likedUsersDataStore.saveLikedUser(userToSave) }
    }

    @Test
    fun `getLikedUsers should return flow from data store`() = runTest {

        val expectedLikedUsers = listOf(User(1, "Liked User", "liked", "liked@example.com", "222", "liked.com"))


        every { likedUsersDataStore.getLikedUsers() } returns flowOf(expectedLikedUsers)


        val actualLikedUsers = userRepositoryImpl.getLikedUsers().first()


        assertEquals(expectedLikedUsers, actualLikedUsers)

        coVerify(exactly = 1) { likedUsersDataStore.getLikedUsers() }
    }
}

