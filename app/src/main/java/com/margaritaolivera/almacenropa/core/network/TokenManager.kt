package com.margaritaolivera.almacenropa.core.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor() {
    private var token: String? = null

    fun saveToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun clearToken() {
        token = null
    }
}