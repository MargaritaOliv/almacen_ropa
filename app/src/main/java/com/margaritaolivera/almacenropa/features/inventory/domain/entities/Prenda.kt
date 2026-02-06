package com.margaritaolivera.almacenropa.features.inventory.domain.entities

data class Prenda(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Double,
    val stock: Int
)