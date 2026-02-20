package com.margaritaolivera.almacenropa.features.auth.data.repositories

import com.margaritaolivera.almacenropa.core.network.TokenManager
import com.margaritaolivera.almacenropa.core.network.WarehouseApi
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.LoginRequest
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.RegisterRequest
import com.margaritaolivera.almacenropa.features.auth.domain.entities.User
import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: WarehouseApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, pass))

            if (!response.token.isNullOrEmpty()) {
                tokenManager.saveToken(response.token)
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

            if (!response.token.isNullOrEmpty()) {
                tokenManager.saveToken(response.token)
            }

            Result.success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}