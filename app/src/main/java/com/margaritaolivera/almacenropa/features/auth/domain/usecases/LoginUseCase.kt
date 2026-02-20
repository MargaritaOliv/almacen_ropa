package com.margaritaolivera.almacenropa.features.auth.domain.usecases
import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String) = repository.login(email, pass)
}