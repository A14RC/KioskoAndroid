package com.kiosko.android.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Inventory : Screen("inventory_screen")
    object Sales : Screen("sales_screen")
}