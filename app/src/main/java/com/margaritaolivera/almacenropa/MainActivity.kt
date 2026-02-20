package com.margaritaolivera.almacenropa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.margaritaolivera.almacenropa.core.navigation.AppNavigation
import com.margaritaolivera.almacenropa.ui.theme.AlmacenRopaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenRopaTheme {
                AppNavigation()
            }
        }
    }
}