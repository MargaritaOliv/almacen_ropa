package com.margaritaolivera.almacenropa.core.di

import com.margaritaolivera.almacenropa.core.hardware.data.AndroidFlashManager
import com.margaritaolivera.almacenropa.core.hardware.data.AndroidVibrationManager
import com.margaritaolivera.almacenropa.core.hardware.domain.FlashManager
import com.margaritaolivera.almacenropa.core.hardware.domain.VibrationManager
import com.margaritaolivera.almacenropa.core.hardware.data.AndroidCameraManager
import com.margaritaolivera.almacenropa.core.hardware.domain.CameraManager
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

    @Binds
    @Singleton
    abstract fun bindCameraManager(impl: AndroidCameraManager): CameraManager
}