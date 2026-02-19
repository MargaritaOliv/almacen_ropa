package com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.mapper

import com.margaritaolivera.almacenropa.features.auth.data.datasources.remote.model.AuthResponse
import com.margaritaolivera.almacenropa.features.auth.domain.entities.User

fun AuthResponse.toDomain(): User {
    // La API devuelve "token" y un objeto "user" (id, nombre)
    // Combinamos esto en una sola entidad de Dominio
    return User(
        id = this.user?.id ?: this.userId ?: 0,
        nombre = this.user?.nombre ?: "Usuario",
        token = this.token ?: ""
    )
}