package com.example.laundryapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.laundryapp.ui.theme.LaundryAppTheme
import com.example.laundryapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // 1. Inisialisasi ViewModel
) {
    // 2. State untuk menampung input pengguna
    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noTelepon by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 3. Ambil state dari ViewModel
    val authState = authViewModel.state
    val context = LocalContext.current

    // 4. Efek samping untuk bereaksi terhadap perubahan state
    LaunchedEffect(authState.registerSuccess) {
        if (authState.registerSuccess) {
            Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
            // Kembali ke halaman login setelah registrasi berhasil
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    // 5. Tampilan (UI)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Akun Baru") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Satu Langkah Lagi!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Isi data diri Anda di bawah ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Form-form input
                OutlinedTextField(value = namaLengkap, onValueChange = { namaLengkap = it }, label = { Text("Nama Lengkap") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = noTelepon, onValueChange = { noTelepon = it }, label = { Text("Nomor Telepon") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(32.dp))

                // Tombol Register
                Button(
                    onClick = {
                        authViewModel.register(namaLengkap, email, noTelepon, password)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !authState.isLoading
                ) {
                    Text(text = "Register", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Link ke Halaman Login
                ClickableText(
                    text = AnnotatedString("Sudah punya akun? Login di sini"),
                    onClick = {
                        // popUpTo("login") agar tidak menumpuk halaman login
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    style = TextStyle(textAlign = TextAlign.Center, color = Color.Gray)
                )
            }
            // 6. Indikator Loading
            if (authState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

// Preview untuk memudahkan desain
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    LaundryAppTheme {
        RegisterScreen(navController = rememberNavController())
    }
}