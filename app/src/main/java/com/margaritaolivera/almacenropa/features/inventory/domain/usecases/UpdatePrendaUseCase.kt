package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import java.io.File
import javax.inject.Inject

class UpdatePrendaUseCase @Inject constructor(private val repository: InventoryRepository) {
    suspend operator fun invoke(prenda: Prenda, imageFile: File? = null): Result<Boolean> = repository.updatePrenda(prenda, imageFile)
}