package com.margaritaolivera.almacenropa.features.inventory.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.FormViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrendaFormScreen(
    viewModel: FormViewModel,
    prendaId: Int?,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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
    var imageFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (!success) {
                imageFile = null
                photoUri = null
            }
        }
    )

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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Foto de la prenda",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                OutlinedButton(
                    onClick = {
                        val file = File.createTempFile("prenda_", ".jpg", context.cacheDir)
                        imageFile = file
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        photoUri = uri
                        cameraLauncher.launch(uri)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (photoUri == null) "Tomar Foto" else "Cambiar Foto")
                }

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
                    onClick = {
                        viewModel.savePrenda(nombre, categoria, talla, precio, stock, imageFile)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(if (prendaId == null) "Guardar Prenda" else "Actualizar Prenda")
                    }
                }
            }
        }
    }
}