package com.example.laundryapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundryapp.data.api.RetrofitInstance
import com.example.laundryapp.data.datastore.UserPreferences
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf(AuthUiState())
        private set

    private val userPreferences = UserPreferences(application)

    fun login(email: String, password: String) {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(email, password)
                // Langsung simpan token ke DataStore. UI akan otomatis bereaksi.
                userPreferences.saveAuthToken(response.token)
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Terjadi kesalahan")
            }
        }
    }
    fun register(namaLengkap: String, email: String, noTelepon: String, password: String) {
        state = state.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Panggil API
                val response = RetrofitInstance.api.register(namaLengkap, email, noTelepon, password)

                // Jika sukses, ubah state
                state = state.copy(isLoading = false, registerSuccess = true)

            } catch (e: Exception) {
                // Jika gagal, simpan pesan error
                state = state.copy(isLoading = false, error = e.message ?: "Terjadi kesalahan")
            }
        }
    }

    // Fungsi untuk "mereset" state setelah pesan error ditampilkan
    fun clearError() {
        state = state.copy(error = null)
    }
}