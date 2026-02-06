package com.margaritaolivera.almacenropa.features.inventory.data.repositories

import com.margaritaolivera.almacenropa.core.di.AppContainer
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDto
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.StockUpdateDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository

class InventoryRepositoryImpl(private val appContainer: AppContainer) : InventoryRepository {

    private val api = appContainer.warehouseApi

    override suspend fun getPrendas(): Result<List<Prenda>> {
        return try {
            val dtos = api.getPrendas()
            val prendas = dtos.map { it.toDomain() }
            Result.success(prendas)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getPrendaById(id: Int): Result<Prenda> {
        return try {
            val dto = api.getPrendaById(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPrenda(prenda: Prenda): Result<Prenda> {
        return try {
            val response = api.createPrenda(prenda.toDto())
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePrenda(prenda: Prenda): Result<Boolean> {
        return try {
            api.updatePrenda(prenda.id, prenda.toDto())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePrenda(id: Int): Result<Boolean> {
        return try {
            api.deletePrenda(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStock(id: Int, cantidad: Int): Result<Prenda> {
        return try {
            val response = api.updateStock(id, StockUpdateDto(cantidad))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}