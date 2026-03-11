package com.margaritaolivera.almacenropa.core.network

import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.AuthResponse
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.LoginRequest
import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.RegisterRequest
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.PrendaDto
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.StockUpdateDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface WarehouseApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("prendas")
    suspend fun getPrendas(): List<PrendaDto>

    @GET("prendas/{id}")
    suspend fun getPrendaById(@Path("id") id: Int): PrendaDto

    @Multipart
    @POST("prendas")
    suspend fun createPrenda(
        @Part("nombre") nombre: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("talla") talla: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): PrendaDto

    @PUT("prendas/{id}")
    suspend fun updatePrenda(@Path("id") id: Int, @Body prenda: PrendaDto): Map<String, String>

    @DELETE("prendas/{id}")
    suspend fun deletePrenda(@Path("id") id: Int): Map<String, String>

    @GET("prendas/categoria/{categoria}")
    suspend fun getPrendasByCategoria(@Path("categoria") categoria: String): List<PrendaDto>

    @PATCH("prendas/stock/{id}")
    suspend fun updateStock(@Path("id") id: Int, @Body stockUpdate: StockUpdateDto): PrendaDto
}