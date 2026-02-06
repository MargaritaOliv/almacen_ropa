package com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper

// IMPORT CORREGIDO: Apunta al paquete de inventory
import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.PrendaDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

// De DTO a Dominio
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

// De Dominio a DTO
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