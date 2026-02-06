package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

data class FormUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)