package com.margaritaolivera.almacenropa.features.inventory.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.presentation.components.PrendaCard
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: InventoryViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var prendaToDelete by remember { mutableStateOf<Prenda?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPrendas()
    }

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
            Column(modifier = Modifier.statusBarsPadding()) {
               Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CLOSET",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Inventario de Ropa",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            }
                            Row {
                                IconButton(onClick = { isSearchActive = !isSearchActive }) {
                                    Icon(Icons.Default.Search, "Buscar", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                                IconButton(onClick = onLogout) {
                                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        }

                        if (isSearchActive) {
                            Spacer(modifier = Modifier.height(16.dp))
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
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            
            Button(
                onClick = onNavigateToCreate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Prenda", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.error != null) {
                    Text(text = state.error!!, modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.prendas) { prenda ->
                            PrendaCard(
                                prenda = prenda,
                                onAddStock = { viewModel.addStock(prenda.id, 1) },
                                onEdit = { onNavigateToEdit(prenda.id) },
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
}