package com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model


data class PrendaDto(
    val id: Int? = null, // Puede ser nulo al crear
    val nombre: String,
    val categoria: String?,
    val talla: String?,
    val precio: Double,
    val stock: Int
)

// DTO para actualizar solo stock (PATCH)
data class StockUpdateDto(
    val cantidad: Int
)