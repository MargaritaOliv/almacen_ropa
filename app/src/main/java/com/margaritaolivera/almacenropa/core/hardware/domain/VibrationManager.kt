package com.margaritaolivera.almacenropa.core.hardware.domain

interface VibrationManager {
    fun vibrate(durationMillis: Long = 100)
}