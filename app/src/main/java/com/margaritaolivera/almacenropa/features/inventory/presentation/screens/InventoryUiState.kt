package com.margaritaolivera.almacenropa.features.inventory.presentation.screens

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

data class InventoryUiState(
    val isLoading: Boolean = false,
    val prendas: List<Prenda> = emptyList(),
    val error: String? = null
)