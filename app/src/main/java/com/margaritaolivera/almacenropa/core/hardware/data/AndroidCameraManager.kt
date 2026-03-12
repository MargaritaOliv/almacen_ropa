package com.margaritaolivera.almacenropa.core.hardware.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.margaritaolivera.almacenropa.core.hardware.domain.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    override fun createTempImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}