package com.margaritaolivera.almacenropa.core.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object Register : AppScreens("register")
    object Dashboard : AppScreens("dashboard")
    object PrendaForm : AppScreens("prenda_form?id={id}") {
        fun createRoute(id: Int? = null): String {
            return if (id != null) "prenda_form?id=$id" else "prenda_form"
        }
    }
}