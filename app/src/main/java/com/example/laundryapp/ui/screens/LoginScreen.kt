package com.example.laundryapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.laundryapp.R
import com.example.laundryapp.ui.theme.LaundryAppTheme
import com.example.laundryapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // 1. Inisialisasi ViewModel
) {
    // 2. State untuk menampung input pengguna
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 3. Ambil state dari ViewModel untuk mengamati perubahan
    val authState = authViewModel.state
    val context = LocalContext.current

    LaunchedEffect(authState.error) {
        authState.error?.let {
            // Tampilkan pesan error jika ada
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            authViewModel.clearError() // Reset error setelah ditampilkan
        }
    }

    // 5. Tampilan (UI)
    Scaffold { paddingValues ->
        // Box digunakan untuk menumpuk elemen, dalam kasus ini menumpuk loading indicator di atas form
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.my_laundry_logo),
                    contentDescription = "Logo Laundry",
                    modifier = Modifier.height(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Judul
                Text(
                    text = "Selamat Datang Kembali",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Login untuk melanjutkan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Form Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Form Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Tombol Login
                Button(
                    onClick = {
                        // Perintahkan ViewModel untuk melakukan login
                        authViewModel.login(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !authState.isLoading // Tombol disable saat loading
                ) {
                    Text(text = "Login", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Link ke Halaman Register
                ClickableText(
                    text = AnnotatedString("Belum punya akun? Daftar di sini"),
                    onClick = {
                        navController.navigate("register")
                    },
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                )
            }

            // 6. Tampilkan Indikator Loading jika state isLoading adalah true
            if (authState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

// Preview untuk memudahkan desain
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LaundryAppTheme {
        LoginScreen(navController = rememberNavController())
    }
}