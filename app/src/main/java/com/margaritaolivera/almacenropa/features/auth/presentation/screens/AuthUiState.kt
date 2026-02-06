package com.margaritaolivera.almacenropa.features.auth.presentation.screens

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false, // True cuando login/registro funciona
    val error: String? = null
)