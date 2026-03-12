package com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.mapper

import com.margaritaolivera.almacenropa.features.inventory.data.datasources.remote.model.PrendaDto
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.core.database.entities.PrendaEntity

fun PrendaDto.toDomain(): Prenda {
    return Prenda(
        id = this.id ?: 0,
        nombre = this.nombre,
        categoria = this.categoria ?: "Sin categoría",
        talla = this.talla ?: "N/A",
        precio = this.precio,
        stock = this.stock,
        imagen = this.imagen
    )
}

fun Prenda.toDto(): PrendaDto {
    return PrendaDto(
        id = if (this.id == 0) null else this.id,
        nombre = this.nombre,
        categoria = this.categoria,
        talla = this.talla,
        precio = this.precio,
        stock = this.stock,
        imagen = this.imagen
    )
}

fun PrendaEntity.toDomain(): Prenda {
    return Prenda(
        id = this.remoteId ?: this.id,
        nombre = this.nombre,
        categoria = this.categoria,
        talla = this.talla,
        precio = this.precio,
        stock = this.stock,
        imagen = this.imagen ?: this.localPath
    )
}

fun Prenda.toEntity(isPending: Boolean = false, localPath: String? = null): PrendaEntity {
    return PrendaEntity(
        remoteId = if (this.id == 0) null else this.id,
        nombre = this.nombre,
        categoria = this.categoria,
        talla = this.talla,
        precio = this.precio,
        stock = this.stock,
        imagen = this.imagen,
        isPending = isPending,
        localPath = localPath
    )
}