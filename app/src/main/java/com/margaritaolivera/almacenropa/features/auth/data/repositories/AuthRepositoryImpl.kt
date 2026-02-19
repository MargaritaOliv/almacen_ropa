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

            if (!response.token.isNullOrEmpty()) {
                appContainer.sessionToken = response.token
            }
            Result.success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, pass: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(nombre, email, pass))

            // Ahora que el backend devuelve token al registrar, lo guardamos también.
            if (!response.token.isNullOrEmpty()) {
                appContainer.sessionToken = response.token
            }

            Result.success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}