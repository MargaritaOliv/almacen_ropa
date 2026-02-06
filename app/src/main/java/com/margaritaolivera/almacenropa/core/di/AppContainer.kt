package com.margaritaolivera.almacenropa.core.di


import android.content.Context
import com.margaritaolivera.almacenropa.core.network.WarehouseApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {

    // URL base de tu API
    private val baseUrl = "https://api-almacen.margaritaydidi.xyz/api/"

    // Variable para almacenar el token en memoria durante la sesión
    var sessionToken: String? = null

    // Cliente HTTP con Interceptor para añadir el Token automáticamente
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            // Si tenemos token, lo agregamos al header
            sessionToken?.let { token ->
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient) // Usamos nuestro cliente personalizado
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instancia única de la API
    val warehouseApi: WarehouseApi by lazy {
        retrofit.create(WarehouseApi::class.java)
    }

    // Aquí inicializaremos los repositorios en los siguientes pasos
    // val authRepository: AuthRepository by lazy { ... }
    // val inventoryRepository: InventoryRepository by lazy { ... }
}