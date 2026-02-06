package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.CreatePrendaUseCase
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.FormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormViewModel(
    private val createPrendaUseCase: CreatePrendaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    fun savePrenda(
        nombre: String,
        categoria: String,
        talla: String,
        precioStr: String,
        stockStr: String
    ) {
        // 1. Validaciones básicas
        if (nombre.isBlank() || categoria.isBlank() || talla.isBlank()) {
            _uiState.update { it.copy(error = "Por favor completa todos los campos de texto") }
            return
        }

        val precio = precioStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null) {
            _uiState.update { it.copy(error = "El precio y stock deben ser números válidos") }
            return
        }

        // 2. Preparar envío
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Creamos la entidad (ID = 0 porque es nueva)
            val newPrenda = Prenda(
                id = 0,
                nombre = nombre,
                categoria = categoria,
                talla = talla,
                precio = precio,
                stock = stock
            )

            // 3. Llamar al caso de uso
            createPrendaUseCase(newPrenda).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al guardar") }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { FormUiState() }
    }
}