package com.example.laundryapp.data

// Untuk respons dari API login
data class LoginResponse(
    val message: String,
    val token: String
)

// Untuk respons sukses
data class GeneralResponse(
    val status: String,
    val message: String
)

// Untuk data Layanan
data class Layanan(
    val id: Int,
    val nama_layanan: String,
    val tipe_layanan: String,
    val harga: String,
    val estimasi_waktu: String
)

// Untuk data Pesanan
data class Pesanan(
    val id: Int,
    val kode_invoice: String,
    val status_pesanan: String,
    val total_harga: String,
    val tanggal_pesan: String
)