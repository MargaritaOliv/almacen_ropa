package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.CreatePrendaUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.GetPrendaByIdUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.UpdatePrendaUseCase

class FormViewModelFactory(
    private val createPrendaUseCase: CreatePrendaUseCase,
    private val getPrendaByIdUseCase: GetPrendaByIdUseCase,
    private val updatePrendaUseCase: UpdatePrendaUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormViewModel(
                createPrendaUseCase,
                getPrendaByIdUseCase,
                updatePrendaUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}