package com.margaritaolivera.almacenropa.core.hardware.domain

interface FlashManager {
    fun turnOn()
    fun turnOff()
    suspend fun blink(durationMillis: Long = 100)
    fun hasFlash(): Boolean
}