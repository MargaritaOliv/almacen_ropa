package com.margaritaolivera.almacenropa.features.inventory.domain.repositories

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import java.io.File

interface InventoryRepository {
    suspend fun getPrendas(): Result<List<Prenda>>
    suspend fun getPrendaById(id: Int): Result<Prenda>
    suspend fun createPrenda(prenda: Prenda, imageFile: File? = null): Result<Prenda>
    suspend fun updatePrenda(prenda: Prenda, imageFile: File? = null): Result<Boolean>
    suspend fun deletePrenda(id: Int): Result<Boolean>
    suspend fun updateStock(id: Int, cantidad: Int): Result<Prenda>
}