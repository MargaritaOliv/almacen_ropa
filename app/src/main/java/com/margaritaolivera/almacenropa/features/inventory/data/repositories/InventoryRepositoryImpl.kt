package com.margaritaolivera.almacenropa.features.inventory.data.repositories

import com.margaritaolivera.almacenropa.core.network.WarehouseApi
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDto
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.StockUpdateDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val api: WarehouseApi
) : InventoryRepository {

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

    override suspend fun createPrenda(prenda: Prenda, imageFile: File?): Result<Prenda> {
        return try {
            val nombreBody = prenda.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoriaBody = prenda.categoria.toRequestBody("text/plain".toMediaTypeOrNull())
            val tallaBody = prenda.talla.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioBody = prenda.precio.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val stockBody = prenda.stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imagenPart = imageFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("imagen", it.name, requestFile)
            }

            val response = api.createPrenda(
                nombre = nombreBody,
                categoria = categoriaBody,
                talla = tallaBody,
                precio = precioBody,
                stock = stockBody,
                imagen = imagenPart
            )
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