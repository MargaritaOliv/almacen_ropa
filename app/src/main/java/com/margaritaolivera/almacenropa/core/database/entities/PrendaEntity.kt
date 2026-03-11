package com.margaritaolivera.almacenropa.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prendas")
data class PrendaEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Double,
    val stock: Int,
    val imagen: String?
)