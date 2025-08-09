package com.example.laundryapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.laundryapp.data.Layanan
import com.example.laundryapp.viewmodel.CreatePesananViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePesananScreen(
    navController: NavController,
    viewModel: CreatePesananViewModel = viewModel()
) {
    val layananState by viewModel.layananState.collectAsState()
    val keranjangState by viewModel.keranjangState.collectAsState()
    val submitState by viewModel.submitState.collectAsState() // Ambil state submit

    // State untuk dialog dan form
    var showConfirmDialog by remember { mutableStateOf(false) }
    var metodePengiriman by remember { mutableStateOf("antar_jemput") }
    var alamat by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(submitState.isSuccess) {
        if (submitState.isSuccess) {
            Toast.makeText(context, "Pesanan berhasil dibuat!", Toast.LENGTH_LONG).show()
            navController.popBackStack() // Kembali ke HomeScreen
            viewModel.resetSubmitState()
        }
    }
    LaunchedEffect(submitState.error) {
        submitState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetSubmitState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Pesanan Baru") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            // Bottom bar untuk menampilkan ringkasan dan tombol submit
            if (keranjangState.items.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val textTotal = if (keranjangState.items.any { it.key.tipe_layanan == "kiloan" }) {
                            "Akan diinfokan Admin"
                        } else {
                            "Rp ${keranjangState.totalHarga}"
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Estimasi:", fontWeight = FontWeight.Bold)
                            Text(textTotal, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showConfirmDialog = true }, // Tampilkan dialog saat diklik
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !submitState.isLoading // Disable saat loading
                        ) {
                            Text("Lanjutkan")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when {
                layananState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                layananState.error != null -> {
                    Text("Error: ${layananState.error}", modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Layanan Kiloan
                        item {
                            Text("Paket Kiloan", style = MaterialTheme.typography.titleLarge)
                        }
                        items(layananState.layananKiloan) { layanan ->
                            LayananItem(
                                layanan = layanan,
                                isSelected = keranjangState.items.containsKey(layanan),
                                onClick = { viewModel.addItem(layanan) }
                            )
                        }

                        // Layanan Satuan
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Layanan Satuan", style = MaterialTheme.typography.titleLarge)
                        }
                        items(layananState.layananSatuan) { layanan ->
                            LayananItemSatuan(
                                layanan = layanan,
                                quantity = keranjangState.items[layanan] ?: 0,
                                onAdd = { viewModel.addItem(layanan) },
                                onRemove = { viewModel.removeItem(layanan) }
                            )
                        }
                    }
                }
            }
        }
    }

    // TAMPILKAN DIALOG JIKA showConfirmDialog == true
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi Pesanan") },
            text = {
                Column {
                    Text("Lengkapi detail pesanan Anda di bawah ini.")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Opsi Metode Pengiriman
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = metodePengiriman == "antar_jemput", onClick = { metodePengiriman = "antar_jemput" })
                        Text("Antar-Jemput")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = metodePengiriman == "datang_langsung", onClick = { metodePengiriman = "datang_langsung" })
                        Text("Datang Langsung")
                    }

                    // Input Alamat (hanya tampil jika antar-jemput)
                    if (metodePengiriman == "antar_jemput") {
                        OutlinedTextField(
                            value = alamat,
                            onValueChange = { alamat = it },
                            label = { Text("Alamat Penjemputan") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Input Catatan
                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        label = { Text("Catatan (Opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitPesanan(metodePengiriman, alamat, catatan)
                        showConfirmDialog = false
                    },
                    enabled = !submitState.isLoading
                ) {
                    if (submitState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Kirim Pesanan")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

}

// Composable untuk item Kiloan
@Composable
fun LayananItem(layanan: Layanan, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(layanan.nama_layanan, fontWeight = FontWeight.Bold)
                Text("Rp ${layanan.harga}/kg", style = MaterialTheme.typography.bodyMedium)
            }
            if (isSelected) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Terpilih", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// Composable untuk item Satuan
@Composable
fun LayananItemSatuan(
    layanan: Layanan,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(layanan.nama_layanan, fontWeight = FontWeight.Bold)
                Text("Rp ${layanan.harga}/pcs", style = MaterialTheme.typography.bodyMedium)
            }
            // Kontrol Jumlah
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemove, enabled = quantity > 0) {
                    Icon(Icons.Filled.Delete, contentDescription = "Kurangi")
                }
                Text("$quantity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                IconButton(onClick = onAdd) {
                    Icon(Icons.Filled.AddCircle, contentDescription = "Tambah")
                }
            }
        }
    }
}