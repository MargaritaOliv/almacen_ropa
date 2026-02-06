package com.margaritaolivera.almacenropa.features.inventory.domain.repositories

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

interface InventoryRepository {
    suspend fun getPrendas(): Result<List<Prenda>>
    suspend fun getPrendaById(id: Int): Result<Prenda>
    suspend fun createPrenda(prenda: Prenda): Result<Prenda>
    suspend fun updatePrenda(prenda: Prenda): Result<Boolean>
    suspend fun deletePrenda(id: Int): Result<Boolean>
    suspend fun updateStock(id: Int, cantidad: Int): Result<Prenda>
}