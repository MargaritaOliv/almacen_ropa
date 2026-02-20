package com.margaritaolivera.almacenropa.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.margaritaolivera.almacenropa.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.almacenropa.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.DashboardScreen
import com.margaritaolivera.almacenropa.features.inventory.presentation.screens.PrendaFormScreen
import com.margaritaolivera.almacenropa.features.auth.presentation.viewmodels.AuthViewModel
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.InventoryViewModel
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.FormViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route
    ) {

        composable(AppScreens.Login.route) {

            val viewModel: AuthViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(AppScreens.Dashboard.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(AppScreens.Register.route) }
            )
        }

        composable(AppScreens.Register.route) {
            val viewModel: AuthViewModel = hiltViewModel()

            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(AppScreens.Dashboard.route) {
            val viewModel: InventoryViewModel = hiltViewModel()

            DashboardScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(AppScreens.PrendaForm.createRoute(null))
                },
                onNavigateToEdit = { id ->
                    navController.navigate(AppScreens.PrendaForm.createRoute(id))
                },
                onLogout = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AppScreens.PrendaForm.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val viewModel: FormViewModel = hiltViewModel()

            PrendaFormScreen(
                viewModel = viewModel,
                prendaId = if (id != -1) id else null,
                onBack = { navController.popBackStack() }
            )
        }
    }
}