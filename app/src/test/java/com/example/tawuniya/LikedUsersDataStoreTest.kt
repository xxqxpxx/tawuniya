package com.example.tawuniya


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
 import com.example.tawuniya.data.local.LikedUsersDataStore
import com.example.tawuniya.domain.model.User
import com.google.gson.Gson
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@ExperimentalCoroutinesApi
class LikedUsersDataStoreTest {


    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().build()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var likedUsersDataStore: LikedUsersDataStore
    private lateinit var gson: Gson

    @Before
    fun setUp() {

        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = {
                tmpFolder.newFolder ("test_prefs")
            }
        )


        gson = Gson()


        likedUsersDataStore = LikedUsersDataStore(
            context = mockk(relaxed = true),

            gson = gson
        )

    }

    @Test
    fun `saveLikedUser should add user to preferences`() = runTest {
        val user1 = User(1, "User One", "user1", "u1@example.com", "111", "one.com")
        val user2 = User(2, "User Two", "user2", "u2@example.com", "222", "two.com")

         likedUsersDataStore.saveLikedUser(user1)

         var likedUsers = likedUsersDataStore.getLikedUsers().first()
        assertEquals(1, likedUsers.size)
        assertEquals(user1, likedUsers[0])

         likedUsersDataStore.saveLikedUser(user2)

         likedUsers = likedUsersDataStore.getLikedUsers().first()
        assertEquals(2, likedUsers.size)
        assertTrue(likedUsers.contains(user1))
        assertTrue(likedUsers.contains(user2))
    }

    @Test
    fun `saveLikedUser should not add duplicate users`() = runTest {
        val user1 = User(1, "User One", "user1", "u1@example.com", "111", "one.com")

         likedUsersDataStore.saveLikedUser(user1)
        likedUsersDataStore.saveLikedUser(user1)

         val likedUsers = likedUsersDataStore.getLikedUsers().first()
        assertEquals(1, likedUsers.size)
        assertEquals(user1, likedUsers[0])
    }

    @Test
    fun `getLikedUsers should return empty list if no users saved`() = runTest {
         val likedUsers = likedUsersDataStore.getLikedUsers().first()
        assertTrue(likedUsers.isEmpty())
    }

    @Test
    fun `getLikedUsers should return correct list of users`() = runTest {
        val user1 = User(1, "User One", "user1", "u1@example.com", "111", "one.com")
        val user2 = User(2, "User Two", "user2", "u2@example.com", "222", "two.com")

        likedUsersDataStore.saveLikedUser(user1)
        likedUsersDataStore.saveLikedUser(user2)

        val likedUsers = likedUsersDataStore.getLikedUsers().first()
        assertEquals(2, likedUsers.size)
        assertTrue(likedUsers.contains(user1))
        assertTrue(likedUsers.contains(user2))
    }
}
