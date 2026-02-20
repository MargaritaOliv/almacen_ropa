package com.margaritaolivera.almacenropa.features.inventory.domain.usecases
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import javax.inject.Inject

class GetPrendasUseCase @Inject constructor(private val repository: InventoryRepository) {
    suspend operator fun invoke(): Result<List<Prenda>> = repository.getPrendas()
}