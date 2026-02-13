package com.kiosko.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiosko.android.presentation.screens.home.HomeScreen
import com.kiosko.android.presentation.screens.inventory.InventoryScreen
import com.kiosko.android.presentation.screens.login.LoginScreen
import com.kiosko.android.presentation.screens.sales.SalesScreen
import com.kiosko.android.presentation.screens.reports.ReportsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla Login
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Home (Dashboard)
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateTo = { route ->
                    navController.navigate(route)
                }
            )
        }

        // Pantalla Inventario
        composable(route = Screen.Inventory.route) {
            InventoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Ventas
        composable(route = Screen.Sales.route) {
            SalesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Reportes (NUEVA)
        composable(route = Screen.Reports.route) {
            ReportsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}