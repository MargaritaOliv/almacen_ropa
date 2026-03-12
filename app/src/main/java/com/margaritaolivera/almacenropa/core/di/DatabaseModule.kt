package com.margaritaolivera.almacenropa.core.di

import android.content.Context
import androidx.room.Room
import com.margaritaolivera.almacenropa.core.database.AppDatabase
import com.margaritaolivera.almacenropa.core.database.dao.PrendaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AlmacenRopaDB"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun providePrendaDao(db: AppDatabase): PrendaDao = db.prendaDao()
}