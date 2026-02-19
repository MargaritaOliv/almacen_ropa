package com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model
import com.google.gson.annotations.SerializedName

// Petición para Login
data class LoginRequest(
    val email: String,
    val password: String
)

// Petición para Registro
data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

// Respuesta de Auth (contiene el token y usuario)
data class AuthResponse(
    val token: String? = null,
    val user: UserDto? = null,
    val msg: String? = null, // Para mensajes de error o éxito
    val userId: Int? = null   // En registro devuelve userId
)

data class UserDto(
    val id: Int,
    val nombre: String
)