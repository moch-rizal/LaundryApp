package com.example.laundryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundryapp.data.Layanan
import com.example.laundryapp.data.api.RetrofitInstance
import com.example.laundryapp.data.datastore.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// State untuk menampung data layanan
data class LayananState(
    val isLoading: Boolean = true,
    val layananKiloan: List<Layanan> = emptyList(),
    val layananSatuan: List<Layanan> = emptyList(),
    val error: String? = null
)

// State untuk menampung item yang dipilih pelanggan (keranjang)
data class KeranjangState(
    val items: Map<Layanan, Int> = emptyMap(), // Map<Layanan, Jumlah>
    val totalHarga: Double = 0.0
)

// State baru untuk hasil submit
data class SubmitState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class CreatePesananViewModel(application: Application) : AndroidViewModel(application) {

    private val _layananState = MutableStateFlow(LayananState())
    val layananState = _layananState.asStateFlow()

    private val _keranjangState = MutableStateFlow(KeranjangState())
    val keranjangState = _keranjangState.asStateFlow()

    // State baru untuk proses submit
    private val _submitState = MutableStateFlow(SubmitState())
    val submitState = _submitState.asStateFlow()

    private val userPreferences = UserPreferences(application)

    init {
        loadLayanan()
    }

    // --- FUNGSI UNTUK MENGAMBIL DATA LAYANAN ---
    private fun loadLayanan() {
        _layananState.value = LayananState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLayanan()
                _layananState.value = LayananState(
                    isLoading = false,
                    layananKiloan = response.filter { it.tipe_layanan == "kiloan" },
                    layananSatuan = response.filter { it.tipe_layanan == "satuan" }
                )
            } catch (e: Exception) {
                _layananState.value = LayananState(isLoading = false, error = e.message)
            }
        }
    }

    // --- FUNGSI UNTUK MENGELOLA KERANJANG ---
    fun addItem(layanan: Layanan) {
        val currentItems = _keranjangState.value.items.toMutableMap()
        if (layanan.tipe_layanan == "kiloan") {
            // Jika kiloan, hapus semua item lain dan tambahkan ini
            currentItems.clear()
            currentItems[layanan] = 1
        } else {
            // Jika satuan, hapus kiloan dan update jumlah
            currentItems.keys.removeAll { it.tipe_layanan == "kiloan" }
            val currentQty = currentItems[layanan] ?: 0
            currentItems[layanan] = currentQty + 1
        }
        updateKeranjang(currentItems)
    }

    fun removeItem(layanan: Layanan) {
        val currentItems = _keranjangState.value.items.toMutableMap()
        val currentQty = currentItems[layanan] ?: 0
        if (currentQty > 1 && layanan.tipe_layanan == "satuan") {
            currentItems[layanan] = currentQty - 1
        } else {
            currentItems.remove(layanan)
        }
        updateKeranjang(currentItems)
    }

    private fun updateKeranjang(items: Map<Layanan, Int>) {
        var total = 0.0
        items.forEach { (layanan, jumlah) ->
            if (layanan.tipe_layanan == "satuan") {
                total += layanan.harga.toDouble() * jumlah
            }
        }
        _keranjangState.value = KeranjangState(items = items, totalHarga = total)
    }

    // --- FUNGSI UNTUK MENGIRIM PESANAN (AKAN KITA BUAT NANTI) ---
    fun submitPesanan(metodePengiriman: String, alamat: String, catatan: String) {
        _submitState.value = SubmitState(isLoading = true)
        viewModelScope.launch {
            try {
                // Siapkan data dalam format yang dibutuhkan API
                val itemsForApi = _keranjangState.value.items.map { (layanan, jumlah) ->
                    mapOf("id" to layanan.id, "tipe" to layanan.tipe_layanan, "jumlah" to jumlah)
                }

                val requestBody = mapOf(
                    "metode_pengiriman" to metodePengiriman,
                    "alamat" to alamat,
                    "catatan" to catatan,
                    "items" to itemsForApi
                )

                // Ambil token dan panggil API
                val token = userPreferences.authToken.first()
                if (token == null) throw Exception("Token tidak ditemukan")

                RetrofitInstance.api.createPesanan("Bearer $token", requestBody)

                _submitState.value = SubmitState(isLoading = false, isSuccess = true)

            } catch (e: Exception) {
                _submitState.value = SubmitState(isLoading = false, error = e.message ?: "Gagal mengirim pesanan")
            }
        }
    }

    // Fungsi untuk mereset state submit setelah navigasi
    fun resetSubmitState() {
        _submitState.value = SubmitState()
    }
}