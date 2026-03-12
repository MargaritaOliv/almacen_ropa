package com.margaritaolivera.almacenropa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margaritaolivera.almacenropa.core.database.entities.PrendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrendaDao {
    @Query("SELECT * FROM prendas")
    fun getAllPrendas(): Flow<List<PrendaEntity>>

    @Query("SELECT * FROM prendas")
    suspend fun getAllPrendasList(): List<PrendaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrendas(prendas: List<PrendaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrenda(prenda: PrendaEntity)

    @Query("DELETE FROM prendas WHERE remoteId = :id OR id = :id")
    suspend fun deletePrendaById(id: Int)

    @Query("DELETE FROM prendas WHERE id = :localId")
    suspend fun deleteLocalPrendaById(localId: Int)

    @Query("SELECT * FROM prendas WHERE remoteId = :id OR id = :id")
    suspend fun getPrendaById(id: Int): PrendaEntity?

    @Query("SELECT * FROM prendas WHERE isPending = 1")
    suspend fun getPendingPrendas(): List<PrendaEntity>

    @Query("DELETE FROM prendas WHERE isPending = 0")
    suspend fun clearSyncedPrendas()

    @Query("DELETE FROM prendas")
    suspend fun clearPrendas()
}