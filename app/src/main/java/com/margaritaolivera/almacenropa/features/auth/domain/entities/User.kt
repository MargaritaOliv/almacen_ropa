package com.margaritaolivera.almacenropa.features.auth.domain.entities

data class User(
    val id: Int,
    val nombre: String,
    val token: String
)