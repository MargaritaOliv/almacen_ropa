package com.margaritaolivera.almacenropa.features.inventory.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrendaFormScreen(
    viewModel: FormViewModel,
    prendaId: Int?,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Cargar datos si hay un ID y aún no se han cargado
    LaunchedEffect(prendaId) {
        if (prendaId != null) {
            viewModel.loadPrenda(prendaId)
        }
    }

    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var talla by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var isInitialized by remember { mutableStateOf(false) }

    // Pre-llenar campos cuando llega la info del servidor (solo la primera vez)
    LaunchedEffect(state.initialPrenda) {
        if (state.initialPrenda != null && !isInitialized) {
            nombre = state.initialPrenda!!.nombre
            categoria = state.initialPrenda!!.categoria
            talla = state.initialPrenda!!.talla
            precio = state.initialPrenda!!.precio.toString()
            stock = state.initialPrenda!!.stock.toString()
            isInitialized = true
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, if (prendaId == null) "Creado con éxito" else "Actualizado con éxito", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (prendaId == null) "Nueva Prenda" else "Editar Prenda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        if (state.isLoading && !isInitialized && prendaId != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = talla, onValueChange = { talla = it }, label = { Text("Talla") }, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }

                if (state.error != null) {
                    Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.savePrenda(nombre, categoria, talla, precio, stock) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(if (prendaId == null) "Guardar" else "Actualizar")
                    }
                }
            }
        }
    }
}