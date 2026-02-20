package com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)


data class AuthResponse(
    val token: String? = null,
    val user: UserDto? = null,
    val msg: String? = null,
    val userId: Int? = null
)

data class UserDto(
    val id: Int,
    val nombre: String
)