package com.margaritaolivera.almacenropa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.margaritaolivera.almacenropa.core.di.AppContainer
import com.margaritaolivera.almacenropa.core.navigation.AppNavigation
import com.margaritaolivera.almacenropa.features.auth.di.AuthModule
import com.margaritaolivera.almacenropa.features.inventory.di.InventoryModule
// ESTA ES LA LÍNEA IMPORTANTE QUE ARREGLA EL ERROR:
import com.margaritaolivera.almacenropa.ui.theme.AlmacenRopaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar Contenedor Principal (Retrofit, SharedPrefs, etc)
        val appContainer = AppContainer(applicationContext)

        // 2. Inicializar Módulos de Feature
        val authModule = AuthModule(appContainer)
        val inventoryModule = InventoryModule(appContainer)

        setContent {
            // Aquí se usa el tema que definimos en ui/theme/Theme.kt
            AlmacenRopaTheme {
                // 3. Lanzar navegación pasando los módulos
                AppNavigation(authModule, inventoryModule)
            }
        }
    }
}