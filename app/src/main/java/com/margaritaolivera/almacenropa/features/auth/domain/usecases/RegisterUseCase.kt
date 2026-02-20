package com.margaritaolivera.almacenropa.features.auth.domain.usecases
import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(nombre: String, email: String, pass: String) =
        repository.register(nombre, email, pass)
}