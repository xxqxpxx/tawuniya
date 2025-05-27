package com.example.tawuniya.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.usecase.GetLikedUsersUseCase
import com.example.tawuniya.domain.usecase.GetUsersUseCase
import com.example.tawuniya.domain.usecase.SaveLikedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val saveLikedUserUseCase: SaveLikedUserUseCase,
    private val getLikedUsersUseCase: GetLikedUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    init {
        getUsers()
        collectLikedUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getUsersUseCase().collect { userList ->
                    _uiState.update { it.copy(users = userList) }
                }
            } catch (e: Exception) {
              /*  _uiState.update {
                    it.copy(error = "Error retrieving users: ${e.localizedMessage}")
                }*/

            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun onLikeUser(user: User) {
        viewModelScope.launch {
            saveLikedUserUseCase(user)
        }
    }

    private fun collectLikedUsers() {
        viewModelScope.launch {
            getLikedUsersUseCase().collectLatest { likedUserList ->
                _uiState.update { it.copy(users = likedUserList) }
            }
        }
    }

    fun isUserLiked(user: User): Boolean {
        return _uiState.value.likedUsers.any { it.id == user.id }
    }
}