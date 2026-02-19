package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
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

    // Lista completa original (sin filtrar)
    private var allPrendas: List<Prenda> = emptyList()
    // Texto de búsqueda actual
    private var currentQuery: String = ""

    init {
        loadPrendas()
    }

    fun loadPrendas() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPrendasUseCase().fold(
                onSuccess = { lista ->
                    allPrendas = lista
                    // Aplicamos filtro si ya había algo escrito, si no, mostramos todo
                    filterPrendas(currentQuery)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al cargar") }
                }
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
        filterPrendas(query)
    }

    private fun filterPrendas(query: String) {
        val filtered = if (query.isBlank()) {
            allPrendas
        } else {
            allPrendas.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.categoria.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(isLoading = false, prendas = filtered) }
    }

    fun deletePrenda(id: Int) {
        viewModelScope.launch {
            deletePrendaUseCase(id).fold(
                onSuccess = { loadPrendas() },
                onFailure = { _uiState.update { state -> state.copy(error = "No se pudo eliminar") } }
            )
        }
    }

    fun addStock(id: Int, cantidad: Int) {
        viewModelScope.launch {
            updateStockUseCase(id, cantidad).fold(
                onSuccess = { loadPrendas() },
                onFailure = { _uiState.update { state -> state.copy(error = "Error al actualizar stock") } }
            )
        }
    }
}