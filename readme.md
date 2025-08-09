
# Aplikasi Android Sistem Laundry (Klien) & REST API (Server)

![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)
![Bahasa](https://img.shields.io/badge/Bahasa-Kotlin-purple.svg)
![Backend](https://img.shields.io/badge/Backend-CodeIgniter%204-orange.svg)

Ini adalah proyek aplikasi laundry lengkap dengan arsitektur Client-Server yang dibangun untuk memenuhi tugas Praktikum dan Ujian Akhir Semester.

- **Backend Server (CodeIgniter 4):** Berfungsi sebagai REST API untuk menyediakan dan mengelola semua data aplikasi. Juga memiliki Panel Admin berbasis web.
- **Client App (Android - Jetpack Compose):** Aplikasi native untuk pelanggan yang berinteraksi dengan backend melalui REST API.

---

##  PENTING: Arsitektur Aplikasi
Aplikasi Android ini **TIDAK BISA** berjalan sendiri. Ia adalah aplikasi **klien** yang harus terhubung ke **backend server** (CodeIgniter 4) untuk bisa berfungsi.

Untuk menjalankan dan menguji aplikasi ini, Anda **HARUS** menjalankan kedua bagian proyek: Server Backend terlebih dahulu, kemudian Aplikasi Android.

---

## ✨ Fitur Utama (Aplikasi Android)

- **Otentikasi:** Registrasi dan Login pengguna menggunakan REST API dengan otentikasi JWT (JSON Web Token).
- **CRUD Pesanan:**
  - **Create:** Membuat pesanan laundry baru (kiloan atau satuan) dari aplikasi.
  - **Read:** Melihat daftar riwayat pesanan dengan status yang *real-time*. Tampilan daftar menggunakan `LazyColumn`.
  - **Update:** Membatalkan pesanan yang statusnya belum diproses.
  - **Delete:** Menghapus pesanan dari riwayat.
- **UI Modern:** Dibangun sepenuhnya menggunakan Jetpack Compose dengan arsitektur MVVM (Model-View-ViewModel).

---

## 🛠️ Prasyarat

Sebelum memulai, pastikan perangkat Anda sudah terinstal:
1.  **Untuk Backend:**
    - PHP 8.1+
    - Composer
    - Server Database (MySQL/MariaDB, contoh: XAMPP)
2.  **Untuk Frontend:**
    - Android Studio (versi terbaru direkomendasikan)
    - Emulator Android atau Perangkat Android fisik

---

## 🚀 Panduan Instalasi & Menjalankan Proyek

Ikuti langkah-langkah ini secara berurutan.

### Bagian 1: Menjalankan Backend Server (CodeIgniter)

1.  **Instal Dependensi PHP**
    ```bash
    composer install
    ```

2.  **Konfigurasi Environment (`.env`)**
    - Buka file `.env` dan sesuaikan koneksi database Anda (nama database, user, password).
    - Pastikan juga mengisi `JWT_SECRET_KEY` dengan string acak bebas.

3.  **Setup Database**
    - Buka phpMyAdmin atau tool database Anda.
    - Buat sebuah database baru, misalnya dengan nama `db_Sistem_laundry`.
    - Impor file `db_Sistem_laundry.sql` .

4.  **Jalankan Server Backend**
    Jalankan server dengan perintah berikut agar bisa diakses dari jaringan lokal:
    ```bash
    php spark serve --host 0.0.0.0
    ```
    Server akan berjalan di `http://localhost:8080`. Biarkan terminal ini tetap terbuka.

5.  **Cari Alamat IP Komputer Anda**
    - Buka Command Prompt (CMD) baru.
    - Ketik `ipconfig` (untuk Windows) atau `ifconfig` (untuk Mac/Linux).
    - Cari alamat **IPv4 Address** Anda di bawah koneksi Wi-Fi atau Ethernet. Contoh: `192.168.1.10`.
    - **Catat alamat IP ini.**

### Bagian 2: Menjalankan Aplikasi Android

1.  **Buka Proyek Android**
    - Buka Android Studio.
    - Pilih "Open" dan arahkan ke folder proyek.

2.  **Tunggu Gradle Sync**
    - Biarkan Android Studio mengunduh semua dependensi yang dibutuhkan. Proses ini mungkin memakan waktu beberapa menit.

3.  **Ubah Alamat IP Server (Langkah Paling Penting)**
    - Buka file: `app/src/main/java/com/example/laundryapp/data/api/RetrofitInstance.kt`.
    - Ganti nilai variabel `BASE_URL` dengan **alamat IP** yang Anda catat di Bagian 1 Langkah 5.

    ```kotlin
    // Sebelum diubah:
    // private const val BASE_URL = "http://10.0.2.2:8080/"

    // Sesudah diubah (contoh):
    private const val BASE_URL = "http://192.168.1.10:8080/"
    ```

4.  **Pastikan Jaringan Sama**
    - Pastikan perangkat Android (Emulator atau Fisik) dan komputer Anda terhubung ke **jaringan Wi-Fi yang SAMA**.

5.  **Jalankan Aplikasi**
    - Klik tombol "Run 'app'" (▶️) di Android Studio untuk meng-install dan menjalankan aplikasi di emulator/perangkat Anda.
    - Aplikasi sekarang akan bisa terhubung ke server backend di komputer Anda.

---

## 👤 Akun Demo

- **Admin (Akses via Web):**
  - Buka `http://localhost:8080/login` di browser.
  - **Email:** `admin@laundry.com`
  - **Password:** `admin123`

- **Pelanggan (Akses via Aplikasi Android):**
  - Silakan buat akun baru melalui fitur **Registrasi** di dalam aplikasi Android.

---

Dibuat oleh **Moch. Arif Samsul Rizal**
