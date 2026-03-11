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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrendas(prendas: List<PrendaEntity>)

    @Query("DELETE FROM prendas")
    suspend fun clearPrendas()
}