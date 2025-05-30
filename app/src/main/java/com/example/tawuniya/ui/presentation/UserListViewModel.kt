package com.example.tawuniya.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tawuniya.domain.model.User
import com.example.tawuniya.domain.usecase.GetLikedUsersUseCase
import com.example.tawuniya.domain.usecase.GetUsersUseCase
import com.example.tawuniya.domain.usecase.RemoveLikedUserUseCase
import com.example.tawuniya.domain.usecase.SaveLikedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val saveLikedUserUseCase: SaveLikedUserUseCase,
    private val removeLikedUserUseCase: RemoveLikedUserUseCase,
    private val getLikedUsersUseCase: GetLikedUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            getUsersUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error retrieving users: ${exception.localizedMessage}"
                        )
                    }
                }
                .collect { userList ->
                    _uiState.update {
                        it.copy(
                            users = userList,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onLikeUser(user: User) {
        viewModelScope.launch {
            if (user.isLiked) {
                // Unlike the user
                removeLikedUserUseCase(user)
            } else {
                // Like the user
                saveLikedUserUseCase(user.copy(isLiked = true))
            }
            
            // Update the UI state immediately for better user experience
            _uiState.update { currentState ->
                currentState.copy(
                    users = currentState.users.map { stateUser ->
                        if (stateUser.id == user.id) {
                            stateUser.copy(isLiked = !stateUser.isLiked)
                        } else {
                            stateUser
                        }
                    }
                )
            }
        }
    }

    fun refreshUsers() {
        getUsers()
    }
}