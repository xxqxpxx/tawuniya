package com.example.tawuniya.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tawuniya.domain.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.coroutines.flow.Flow
private val Context.dataStore by preferencesDataStore(name = "liked_users_prefs")

@Singleton
class LikedUsersDataStore @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    private val LIKED_USERS_KEY = stringPreferencesKey("liked_users")
    suspend fun saveLikedUser(user: User) {
        context.dataStore.edit { preferences ->
            val currentLikedUsersJson = preferences[LIKED_USERS_KEY] ?: "[]"
            val type = object : TypeToken<MutableList<User>>() {}.type
            val likedUsers: MutableList<User> = gson.fromJson(currentLikedUsersJson, type)
            if (likedUsers.none { it.id == user.id }) {
                likedUsers.add(user)
            }
            preferences[LIKED_USERS_KEY] = gson.toJson(likedUsers)
        }
    }

    fun getLikedUsers(): Flow<List<User>> {
        return context.dataStore.data.map { preferences ->
            val likedUsersJson = preferences[LIKED_USERS_KEY] ?: "[]"
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(likedUsersJson, type) ?: emptyList()
        }
    }
}