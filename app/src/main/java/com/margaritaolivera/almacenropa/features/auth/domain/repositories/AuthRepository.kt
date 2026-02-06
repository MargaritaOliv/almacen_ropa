package com.margaritaolivera.almacenropa.features.auth.domain.repositories

import com.margaritaolivera.almacenropa.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun register(nombre: String, email: String, pass: String): Result<User>
}