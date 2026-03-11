package com.margaritaolivera.almacenropa.core.di

import com.margaritaolivera.almacenropa.core.hardware.data.AndroidFlashManager
import com.margaritaolivera.almacenropa.core.hardware.data.AndroidVibrationManager
import com.margaritaolivera.almacenropa.core.hardware.domain.FlashManager
import com.margaritaolivera.almacenropa.core.hardware.domain.VibrationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindFlashManager(impl: AndroidFlashManager): FlashManager

    @Binds
    @Singleton
    abstract fun bindVibrationManager(impl: AndroidVibrationManager): VibrationManager
}