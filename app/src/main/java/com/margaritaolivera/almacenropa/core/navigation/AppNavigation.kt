package com.margaritaolivera.almacenropa.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.margaritaolivera.almacenropa.features.auth.di.AuthModule
import com.margaritaolivera.almacenropa.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.almacenropa.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.almacenropa.features.inventory.di.InventoryModule
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.DashboardScreen
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.PrendaFormScreen

@Composable
fun AppNavigation(
    authModule: AuthModule,
    inventoryModule: InventoryModule
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route
    ) {

        // --- AUTH ---
        composable(AppScreens.Login.route) {
            LoginScreen(
                viewModel = viewModel(factory = authModule.provideAuthViewModelFactory()),
                onLoginSuccess = {
                    navController.navigate(AppScreens.Dashboard.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppScreens.Register.route)
                }
            )
        }

        composable(AppScreens.Register.route) {
            RegisterScreen(
                viewModel = viewModel(factory = authModule.provideAuthViewModelFactory()),
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // --- INVENTORY ---
        composable(AppScreens.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel(factory = inventoryModule.provideInventoryViewModelFactory()),
                onNavigateToCreate = {
                    navController.navigate(AppScreens.PrendaForm.route)
                },
                onLogout = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreens.PrendaForm.route) {
            PrendaFormScreen(
                viewModel = viewModel(factory = inventoryModule.provideFormViewModelFactory()),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}