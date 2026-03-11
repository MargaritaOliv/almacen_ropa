package com.margaritaolivera.almacenropa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.margaritaolivera.almacenropa.core.database.dao.PrendaDao
import com.margaritaolivera.almacenropa.core.database.entities.PrendaEntity

@Database(
    entities =[PrendaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prendaDao(): PrendaDao
}