package com.margaritaolivera.almacenropa.core.network

import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.*
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.*
import retrofit2.http.*

interface WarehouseApi {

    // --- AUTH ---
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    // --- PRENDAS (Requieren Token) ---
    // Nota: El token se inyectará automáticamente vía Interceptor en el AppContainer

    @GET("prendas")
    suspend fun getPrendas(): List<PrendaDto>

    @GET("prendas/{id}")
    suspend fun getPrendaById(@Path("id") id: Int): PrendaDto

    @POST("prendas")
    suspend fun createPrenda(@Body prenda: PrendaDto): PrendaDto

    @PUT("prendas/{id}")
    suspend fun updatePrenda(@Path("id") id: Int, @Body prenda: PrendaDto): Map<String, String> // Devuelve msg

    @DELETE("prendas/{id}")
    suspend fun deletePrenda(@Path("id") id: Int): Map<String, String> // Devuelve msg

    @GET("prendas/categoria/{categoria}")
    suspend fun getPrendasByCategoria(@Path("categoria") categoria: String): List<PrendaDto>

    @PATCH("prendas/stock/{id}")
    suspend fun updateStock(@Path("id") id: Int, @Body stockUpdate: StockUpdateDto): PrendaDto
}