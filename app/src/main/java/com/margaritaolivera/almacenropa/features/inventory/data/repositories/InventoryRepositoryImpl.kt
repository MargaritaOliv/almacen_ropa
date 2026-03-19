package com.margaritaolivera.almacenropa.features.inventory.data.repositories

import android.util.Log
import com.margaritaolivera.almacenropa.core.network.WarehouseApi
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDto
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toEntity
import com.margaritaolivera.almacenropa.core.database.dao.PrendaDao
import com.margaritaolivera.almacenropa.core.database.entities.PrendaEntity
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.StockUpdateDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale
import javax.inject.Inject
import androidx.work.*
import com.margaritaolivera.almacenropa.features.inventory.data.workers.SyncWorker
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

class InventoryRepositoryImpl @Inject constructor(
    private val api: WarehouseApi,
    private val dao: PrendaDao,
    @ApplicationContext private val context: Context
) : InventoryRepository {

    override suspend fun getPrendas(): Result<List<Prenda>> {
        return try {
            val dtos = api.getPrendas()
            val domainList = dtos.map { it.toDomain() }
            
            dao.clearSyncedPrendas()
            dao.insertPrendas(domainList.map { it.toEntity() })
            
            val pending = dao.getPendingPrendas().map { it.toDomain() }
            Result.success(domainList + pending)
        } catch (e: Exception) {
            val localItems = dao.getAllPrendasList()
            if (localItems.isNotEmpty()) {
                Result.success(localItems.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPrendaById(id: Int): Result<Prenda> {
        return try {
            val local = dao.getPrendaById(id)
            if (local != null) {
                Result.success(local.toDomain())
            } else {
                val dto = api.getPrendaById(id)
                Result.success(dto.toDomain())
            }
        } catch (e: Exception) {
            dao.getPrendaById(id)?.let {
                Result.success(it.toDomain())
            } ?: Result.failure(e)
        }
    }

    override suspend fun createPrenda(prenda: Prenda, imageFile: File?): Result<Prenda> {
        return try {
            val imagenPart = imageFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("imagen", it.name, requestFile)
            }

            val response = api.createPrenda(
                nombre = prenda.nombre.toPart(),
                categoria = prenda.categoria.toPart(),
                talla = prenda.talla.toPart(),
                precio = String.format(Locale.US, "%.2f", prenda.precio).toPart(),
                stock = prenda.stock.toString().toPart(),
                imagen = imagenPart
            )

            Result.success(response.toDomain())
        } catch (e: Exception) {
            if (e !is retrofit2.HttpException) {
                val pendingEntity = prenda.toEntity(isPending = true, localPath = imageFile?.absolutePath)
                dao.insertPrenda(pendingEntity)
                
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                
                val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                    .setConstraints(constraints)
                    .build()
                
                WorkManager.getInstance(context).enqueueUniqueWork(
                    "SyncWork",
                    ExistingWorkPolicy.REPLACE,
                    syncRequest
                )
                
                Result.success(prenda.copy(id = -1))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun updatePrenda(prenda: Prenda, imageFile: File?): Result<Boolean> {
        return try {
            val localEntity = dao.getPrendaById(prenda.id)
            
            if (localEntity?.isPending == true) {
                val updatedEntity = prenda.toEntity(
                    isPending = true, 
                    localPath = imageFile?.absolutePath ?: localEntity.localPath
                ).copy(id = localEntity.id)
                
                dao.insertPrenda(updatedEntity)
                return Result.success(true)
            }

            val imagenPart = imageFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("imagen", it.name, requestFile)
            }

            api.updatePrenda(
                id = prenda.id,
                nombre = prenda.nombre.toPart(),
                categoria = prenda.categoria.toPart(),
                talla = prenda.talla.toPart(),
                precio = String.format(Locale.US, "%.2f", prenda.precio).toPart(),
                stock = prenda.stock.toString().toPart(),
                imagen = imagenPart
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePrenda(id: Int): Result<Boolean> {
        return try {
            val localEntity = dao.getPrendaById(id)
            if (localEntity?.isPending == true) {
                dao.deleteLocalPrendaById(localEntity.id)
                return Result.success(true)
            }
            
            api.deletePrenda(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStock(id: Int, cantidad: Int): Result<Prenda> {
        return try {
            val localEntity = dao.getPrendaById(id)
            if (localEntity?.isPending == true) {
                val newStock = localEntity.stock + cantidad
                val updatedEntity = localEntity.copy(stock = newStock)
                dao.insertPrenda(updatedEntity)
                return Result.success(updatedEntity.toDomain())
            }

            val response = api.updateStock(id, StockUpdateDto(cantidad))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun String.toPart(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}