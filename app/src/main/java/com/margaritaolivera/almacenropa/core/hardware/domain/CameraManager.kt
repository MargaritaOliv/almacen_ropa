package com.margaritaolivera.almacenropa.core.hardware.domain

import android.net.Uri
import java.io.File

interface CameraManager {
    fun createTempImageFile(): File?
    fun getUriForFile(file: File): Uri
}