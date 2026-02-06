package com.margaritaolivera.almacenropa.features.auth.domain.usecases

import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(nombre: String, email: String, pass: String) =
        repository.register(nombre, email, pass)
}