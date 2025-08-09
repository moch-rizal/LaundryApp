package com.example.laundryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // Import ini
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.laundryapp.ui.screens.CreatePesananScreen
import com.example.laundryapp.ui.screens.HomeScreen
import com.example.laundryapp.ui.screens.LoginScreen
import com.example.laundryapp.ui.screens.RegisterScreen
import com.example.laundryapp.ui.theme.LaundryAppTheme
import com.example.laundryapp.viewmodel.AuthState
import com.example.laundryapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels() // Inisialisasi MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaundryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authState by mainViewModel.authState.collectAsState()
                    val navController = rememberNavController()

                    when (authState) {
                        AuthState.LOADING -> {
                            // Tampilkan loading screen saat pertama kali membuka aplikasi
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        AuthState.UNAUTHENTICATED -> {
                            // Jika tidak ada token, tampilkan grafik navigasi untuk otentikasi
                            NavHost(navController = navController, startDestination = "login") {
                                composable("login") { LoginScreen(navController = navController) }
                                composable("register") { RegisterScreen(navController = navController) }
                            }
                        }
                        AuthState.AUTHENTICATED -> {
                            // Jika ada token, tampilkan grafik navigasi untuk bagian utama aplikasi
                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") { HomeScreen(navController = navController) }
                                // Tambahkan layar lain yang butuh login di sini
                                composable("create_pesanan") { // <-- TAMBAHKAN RUTE BARU INI
                                    CreatePesananScreen(navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}