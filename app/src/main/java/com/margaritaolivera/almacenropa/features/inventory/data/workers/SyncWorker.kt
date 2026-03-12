package com.margaritaolivera.almacenropa.features.inventory.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.margaritaolivera.almacenropa.core.database.dao.PrendaDao
import com.margaritaolivera.almacenropa.core.database.entities.PrendaEntity
import com.margaritaolivera.almacenropa.core.network.WarehouseApi
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toDomain
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper.toEntity
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: PrendaDao,
    private val api: WarehouseApi
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "Iniciando sincronización...")
        val pending = dao.getPendingPrendas()
        Log.d("SyncWorker", "Prendas pendientes encontradas: ${pending.size}")
        if (pending.isEmpty()) return Result.success()

        var allSuccess = true

        pending.forEach { entity ->
            try {
                val imageFile = entity.localPath?.let { File(it) }
                val imagenPart = imageFile?.let {
                    if (it.exists()) {
                        val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("imagen", it.name, requestFile)
                    } else null
                }

                val response = api.createPrenda(
                    nombre = entity.nombre.toPart(),
                    categoria = entity.categoria.toPart(),
                    talla = entity.talla.toPart(),
                    precio = String.format(Locale.US, "%.2f", entity.precio).toPart(),
                    stock = entity.stock.toString().toPart(),
                    imagen = imagenPart
                )

                Log.d("SyncWorker", "Prenda subida con éxito: ${entity.nombre}, ID nuevo: ${response.id}")

                Log.d("SyncWorker", "Eliminando prenda local con ID: ${entity.id}")
                dao.deleteLocalPrendaById(entity.id)
                Log.d("SyncWorker", "Insertando prenda remota con ID: ${response.id} en la base de datos local.")
                dao.insertPrenda(response.toDomain().toEntity())

            } catch (e: Exception) {
                Log.e("SyncWorker", "Error subiendo prenda ${entity.nombre} (ID local: ${entity.id}): ${e.message}", e)
                allSuccess = false
            }
        }

        return if (allSuccess) Result.success() else Result.retry()
    }

    private fun String.toPart(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
