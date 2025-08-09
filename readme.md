# Sistem Informasi Laundry Aplikasi Android

![Platform](https://img.shields.io/badge/Platform-Web%20%26%20Android-brightgreen.svg)
![Backend](https://img.shields.io/badge/Backend-CodeIgniter%204-orange.svg)
![Frontend](https://img.shields.io/badge/Frontend-Jetpack%20Compose-blue.svg)
![Database](https://img.shields.io/badge/Database-MySQL-lightgrey.svg)

## Deskripsi Proyek

Proyek ini adalah sebuah ekosistem Sistem Informasi Laundry lengkap yang dibangun dari dua bagian utama yang saling terintegrasi:

1.  **Aplikasi Web (CodeIgniter 4):** Berfungsi sebagai **Panel Admin** untuk manajemen bisnis dan sebagai **REST API Server** yang menyediakan data untuk aplikasi mobile.
2.  **Aplikasi Mobile (Android - Jetpack Compose):** Berfungsi sebagai **Aplikasi Klien** yang memungkinkan pelanggan untuk melakukan pemesanan dan melihat riwayat transaksi langsung dari ponsel mereka.

**[Link Download Aplikasi Android (.apk)]** - `https://drive.google.com/file/d/15X0Sxz1Xrj0ztA9HYV1YXSVuoEXdr0Lr/view?usp=sharing`

---

## ✨ Fitur Utama
- **UI Modern:** Dibangun sepenuhnya menggunakan Jetpack Compose dengan arsitektur MVVM.
- **Otentikasi Aman:** Registrasi & Login pengguna terhubung ke API dengan otentikasi berbasis JWT (JSON Web Token).
- **CRUD Pesanan:**
  - **Create:** Membuat pesanan laundry baru dengan pilihan layanan kiloan atau satuan.
  - **Read:** Melihat daftar riwayat pesanan secara *real-time*. Halaman akan otomatis me-refresh saat kembali.
  - **Update:** Membatalkan pesanan yang statusnya belum diproses.
  - **Delete:** Menghapus riwayat pesanan dari daftar.
- **Logout:** Mengakhiri sesi pengguna dengan aman.
---

## 🛠️ Panduan Menjalankan Proyek

### Bagian 1: Menjalankan Aplikasi Android

Aplikasi Android ini sudah dikonfigurasi untuk terhubung ke server yang sudah di-hosting. Anda bisa langsung menginstalnya.

1.  **Instalasi Langsung:**
    - Unduh file `.apk` dari link yang disediakan di atas.
    - Pindahkan ke perangkat Android Anda dan instal.
    - Aplikasi siap digunakan.

2.  **Menjalankan dari Android Studio (Jika ingin melihat kode):**
    - **Prasyarat:** Android Studio (versi terbaru).
    - Buka proyek dari folder yang sudah di-clone.
    - Tunggu hingga Gradle Sync selesai.
    - Klik "Run 'app'" (▶️) untuk menjalankan di emulator atau perangkat fisik.

### Catatan Tambahan
- **Akun Pelanggan (Android):** Silakan buat akun baru melalui fitur Registrasi di aplikasi.
- **Akun Demo:** Email: harry@gmail.com - Password: 11111111

---

Dibuat oleh: **Moch. Arif Samsul Rizal**
