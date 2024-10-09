package com.ahmedapps.moviesapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.moviesapp.data.model.User
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    fun loadUserData(username: String) {
        viewModelScope.launch {
            repository.getUser(username) { user ->
                _userState.value = user
            }
        }
    }

    fun deleteUser(userId: String?, onResult: (Boolean) -> Unit) {
        if (userId == null) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            repository.deleteUser(userId) { success ->
                onResult(success)
            }
        }
    }

    fun updateUser(userId: String, updatedUser: User) {
        viewModelScope.launch {
            repository.updateUser(userId, updatedUser) { success ->
                if (success) {
                    _userState.value = updatedUser
                }
            }
        }
    }

    fun logout(context: android.content.Context) {
        viewModelScope.launch {
            repository.logout(context)
            _isLoggedOut.value = true
        }
    }
}
