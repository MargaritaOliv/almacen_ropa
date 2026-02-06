package com.margaritaolivera.almacenropa.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.almacenropa.features.auth.domain.usecases.LoginUseCase
import com.margaritaolivera.almacenropa.features.auth.domain.usecases.RegisterUseCase
import com.margaritaolivera.almacenropa.features.auth.presentation.screens.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            loginUseCase(email, pass).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = "Error: Credenciales inválidas o servidor caído") }
                }
            )
        }
    }

    fun register(nombre: String, email: String, pass: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            registerUseCase(nombre, email, pass).fold(
                onSuccess = {
                    // Nota: Asumimos que registrarse es exitoso pero quizás requiera login manual,
                    // o navegación directa. Marcamos success.
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al registrar") }
                }
            )
        }
    }

    // Resetear estado (útil al navegar entre login y registro)
    fun resetState() {
        _uiState.update { AuthUiState() }
    }
}