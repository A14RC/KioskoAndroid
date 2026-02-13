package com.kiosko.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiosko.android.presentation.screens.home.HomeScreen
import com.kiosko.android.presentation.screens.login.LoginScreen

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
                    // Al loguearse, vamos al Home y borramos el Login del historial (para que 'Atrás' no vuelva al login)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Home
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateTo = { route ->
                    navController.navigate(route)
                }
            )
        }

        // Placeholders para futuras pantallas (para que no de error al clicar)
        composable(route = Screen.Inventory.route) {
            // Aquí pondremos el inventario en el siguiente paso
        }
        composable(route = Screen.Sales.route) {
            // Aquí pondremos las ventas
        }
    }
}