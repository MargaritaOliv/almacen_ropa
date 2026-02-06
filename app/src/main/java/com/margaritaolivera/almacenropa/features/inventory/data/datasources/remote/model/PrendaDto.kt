package com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class PrendaDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String?,
    @SerializedName("talla") val talla: String?,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

// Movemos esta clase aquí porque pertenece al contexto de Inventario
data class StockUpdateDto(
    @SerializedName("cantidad") val cantidad: Int
)