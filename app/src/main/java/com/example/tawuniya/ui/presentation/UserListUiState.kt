package com.example.tawuniya.ui.presentation

import com.example.tawuniya.domain.model.User

data class UserListUiState(
    val users: List<User> = emptyList(),
    val likedUsers: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)