package com.margaritaolivera.almacenropa.features.auth.data.repositories

import com.margaritaolivera.almacenropa.core.di.AppContainer
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.LoginRequest
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.RegisterRequest
import com.margaritaolivera.almacenropa.features.auth.domain.entities.User
import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository

class AuthRepositoryImpl(private val appContainer: AppContainer) : AuthRepository {

    private val api = appContainer.warehouseApi

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, pass))
            // ¡CRUCIAL! Guardamos el token en el container para futuras peticiones
            appContainer.sessionToken = response.token
            Result.success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, pass: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(nombre, email, pass))
            // En algunos flujos, registrarse no loguea automáticamente,
            // pero si tu API devuelve token al registrar, lo guardamos.
            if (response.token.isNotEmpty()) {
                appContainer.sessionToken = response.token
            }
            Result.success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}