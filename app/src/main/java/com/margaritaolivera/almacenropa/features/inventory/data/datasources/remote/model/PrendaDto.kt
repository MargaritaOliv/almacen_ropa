package com.margaritaolivera.almacenropa.features.inventory.data.remote.model

import com.google.gson.annotations.SerializedName

data class PrendaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("talla") val talla: String,
    // Sequelize a veces devuelve strings para decimales, usamos Double por seguridad
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)