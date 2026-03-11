package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.almacenropa.core.hardware.domain.FlashManager
import com.margaritaolivera.almacenropa.core.hardware.domain.VibrationManager
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.CreatePrendaUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.GetPrendaByIdUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.UpdatePrendaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val createPrendaUseCase: CreatePrendaUseCase,
    private val getPrendaByIdUseCase: GetPrendaByIdUseCase,
    private val updatePrendaUseCase: UpdatePrendaUseCase,
    private val vibrationManager: VibrationManager,
    private val flashManager: FlashManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()
    private var currentId: Int? = null

    fun loadPrenda(id: Int) {
        if (currentId == id) return
        currentId = id
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getPrendaByIdUseCase(id).fold(
                onSuccess = { prenda ->
                    _uiState.update { it.copy(isLoading = false, initialPrenda = prenda) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, error = "Error al cargar prenda") }
                }
            )
        }
    }

    fun savePrenda(nombre: String, categoria: String, talla: String, precioStr: String, stockStr: String, imageFile: File?) {

        vibrationManager.vibrate(50)

        if (nombre.isBlank() || categoria.isBlank() || talla.isBlank()) {
            _uiState.update { it.copy(error = "Completa todos los campos") }
            return
        }
        val precio = precioStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null) {
            _uiState.update { it.copy(error = "Precio y stock deben ser números") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val prenda = Prenda(
                id = currentId ?: 0,
                nombre = nombre,
                categoria = categoria,
                talla = talla,
                precio = precio,
                stock = stock
            )

            val result = if (currentId == null || currentId == 0) {
                createPrendaUseCase(prenda, imageFile).map { true }
            } else {
                updatePrendaUseCase(prenda)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }

                    viewModelScope.launch {
                        vibrationManager.vibrate(200)
                        if (flashManager.hasFlash()) {
                            flashManager.blink(300)
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al guardar") }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { FormUiState() }
        currentId = null
    }
}