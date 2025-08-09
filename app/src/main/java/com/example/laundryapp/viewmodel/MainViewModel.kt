package com.example.laundryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundryapp.data.datastore.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    LOADING
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    val authState: StateFlow<AuthState> = userPreferences.authToken
        .map { token ->
            if (token != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.LOADING // State awal adalah loading
        )
    // FUNGSI UNTUK LOGOUT
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearAuthToken() // Panggil fungsi untuk menghapus token
        }
    }
}