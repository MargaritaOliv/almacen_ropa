package com.margaritaolivera.almacenropa.features.auth.domain.usecases

import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String) = repository.login(email, pass)
}