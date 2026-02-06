package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.CreatePrendaUseCase

class FormViewModelFactory(
    private val createPrendaUseCase: CreatePrendaUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormViewModel(createPrendaUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}