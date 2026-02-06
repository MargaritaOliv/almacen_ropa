package com.margaritaolivera.almacenropa.core.navigation


sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object Register : AppScreens("register")
    object Dashboard : AppScreens("dashboard")
    object PrendaForm : AppScreens("prenda_form")
}