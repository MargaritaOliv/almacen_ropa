package com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper

import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.PrendaDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

fun PrendaDto.toDomain(): Prenda {
    return Prenda(
        id = this.id ?: 0,
        nombre = this.nombre,
        categoria = this.categoria ?: "Sin categoría",
        talla = this.talla ?: "N/A",
        precio = this.precio,
        stock = this.stock
    )
}

fun Prenda.toDto(): PrendaDto {
    return PrendaDto(
        id = if (this.id == 0) null else this.id,
        nombre = this.nombre,
        categoria = this.categoria,
        talla = this.talla,
        precio = this.precio,
        stock = this.stock
    )
}