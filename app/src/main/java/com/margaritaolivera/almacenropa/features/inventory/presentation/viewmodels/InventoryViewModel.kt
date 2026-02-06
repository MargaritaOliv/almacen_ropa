package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.DeletePrendaUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.GetPrendasUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.UpdateStockUseCase
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.InventoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val getPrendasUseCase: GetPrendasUseCase,
    private val deletePrendaUseCase: DeletePrendaUseCase,
    private val updateStockUseCase: UpdateStockUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPrendas()
    }

    fun loadPrendas() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPrendasUseCase().fold(
                onSuccess = { lista ->
                    _uiState.update { it.copy(isLoading = false, prendas = lista) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al cargar") }
                }
            )
        }
    }

    fun deletePrenda(id: Int) {
        viewModelScope.launch {
            // Optimistic update: podríamos quitarlo de la lista localmente primero,
            // pero lo haremos recargando para asegurar consistencia.
            deletePrendaUseCase(id).onSuccess {
                loadPrendas()
            }.onFailure {
                _uiState.update { state -> state.copy(error = "No se pudo eliminar") }
            }
        }
    }

    fun addStock(id: Int, cantidad: Int) {
        viewModelScope.launch {
            updateStockUseCase(id, cantidad).onSuccess {
                loadPrendas() // Recargar para ver el stock actualizado
            }.onFailure {
                _uiState.update { state -> state.copy(error = "Error al actualizar stock") }
            }
        }
    }
}