package com.example.laundryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundryapp.data.Pesanan
import com.example.laundryapp.data.api.RetrofitInstance
import com.example.laundryapp.data.datastore.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// State untuk UI HomeScreen
data class PesananUiState(
    val isLoading: Boolean = true,
    val pesananList: List<Pesanan> = emptyList(),
    val error: String? = null
)

class PesananViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PesananUiState())
    val uiState = _uiState.asStateFlow()

    private val userPreferences = UserPreferences(application)

    init {
        // Panggil fungsi untuk memuat data saat ViewModel pertama kali dibuat
        loadPesanan()
    }

    fun loadPesanan() {
        // Set state ke loading
        _uiState.value = PesananUiState(isLoading = true)

        viewModelScope.launch {
            try {
                // 1. Ambil token dari DataStore
                val token = userPreferences.authToken.first()
                if (token == null) {
                    throw Exception("Token tidak ditemukan, silakan login ulang.")
                }

                // 2. Panggil API dengan token
                val response = RetrofitInstance.api.getPesanan("Bearer $token")

                // 3. Update state dengan data yang berhasil didapat
                _uiState.value = PesananUiState(isLoading = false, pesananList = response)

            } catch (e: Exception) {
                // 4. Update state dengan pesan error jika gagal
                _uiState.value = PesananUiState(isLoading = false, error = e.message ?: "Gagal memuat data")
            }
        }
    }

    fun cancelPesanan(pesananId: Int) {
        // Kita tidak set isLoading di sini agar tidak seluruh layar loading,
        // Tapi kita bisa menambahkan state loading per item jika mau (versi lebih advanced)

        viewModelScope.launch {
            try {
                val token = userPreferences.authToken.first()
                if (token == null) throw Exception("Token tidak ditemukan")

                // Panggil API untuk membatalkan
                RetrofitInstance.api.cancelPesanan("Bearer $token", pesananId)

                // Jika berhasil, muat ulang daftar pesanan agar UI terupdate
                loadPesanan()

            } catch (e: Exception) {
                // Jika gagal, update state dengan error
                _uiState.value = _uiState.value.copy(error = e.message ?: "Gagal membatalkan pesanan")
            }
        }
    }

    fun deletePesanan(pesananId: Int) {
        viewModelScope.launch {
            try {
                val token = userPreferences.authToken.first()
                if (token == null) throw Exception("Token tidak ditemukan")

                // Panggil API untuk menghapus
                RetrofitInstance.api.deletePesanan("Bearer $token", pesananId)

                // Jika berhasil, muat ulang daftar pesanan
                loadPesanan()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Gagal menghapus pesanan")
            }
        }
    }

    // Fungsi untuk clear error agar tidak muncul terus
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}