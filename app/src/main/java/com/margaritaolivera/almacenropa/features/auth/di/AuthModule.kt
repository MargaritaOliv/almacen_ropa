package com.margaritaolivera.almacenropa.features.auth.di

import com.margaritaolivera.almacenropa.core.di.AppContainer
import com.margaritaolivera.almacenropa.features.auth.data.repositories.AuthRepositoryImpl
import com.margaritaolivera.almacenropa.features.auth.domain.usecases.LoginUseCase
import com.margaritaolivera.almacenropa.features.auth.domain.usecases.RegisterUseCase
import com.margaritaolivera.almacenropa.features.auth.presentation.viewmodels.AuthViewModelFactory

class AuthModule(private val appContainer: AppContainer) {

    // Creamos el repositorio
    private val authRepository by lazy {
        AuthRepositoryImpl(appContainer)
    }

    // Proveemos la fábrica del ViewModel
    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        return AuthViewModelFactory(
            LoginUseCase(authRepository),
            RegisterUseCase(authRepository)
        )
    }
}