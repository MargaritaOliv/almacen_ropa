package com.margaritaolivera.almacenropa.features.inventory.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.presentation.components.PrendaCard
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: InventoryViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit, // Nueva lambda para editar
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Estado para búsqueda
    var searchQuery by remember { mutableStateOf("") }
    var prendaToDelete by remember { mutableStateOf<Prenda?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadPrendas()
    }

    // Alerta Eliminar
    if (prendaToDelete != null) {
        AlertDialog(
            onDismissRequest = { prendaToDelete = null },
            title = { Text("Eliminar Prenda") },
            text = { Text("¿Eliminar '${prendaToDelete?.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        prendaToDelete?.let {
                            viewModel.deletePrenda(it.id)
                            Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                        }
                        prendaToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { prendaToDelete = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Inventario", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                )
                // BARRA DE BÚSQUEDA
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.onSearchQueryChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar prenda...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.onSearchQueryChanged("")
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Borrar")
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) { Icon(Icons.Default.Add, "Nuevo") }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(text = state.error!!, modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(state.prendas) { prenda ->
                        PrendaCard(
                            prenda = prenda,
                            onAddStock = { viewModel.addStock(prenda.id, 1) },
                            onEdit = { onNavigateToEdit(prenda.id) }, // Navegamos a editar
                            onDelete = { prendaToDelete = prenda }
                        )
                    }
                }
                if (state.prendas.isEmpty() && !state.isLoading) {
                    Text("No hay resultados.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                }
            }
        }
    }
}