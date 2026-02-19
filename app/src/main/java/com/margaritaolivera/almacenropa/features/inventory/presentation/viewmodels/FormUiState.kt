package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

data class FormUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val initialPrenda: Prenda? = null
)