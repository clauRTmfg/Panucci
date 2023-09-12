package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.MenuListScreen

internal const val menuRoute = "menu"

fun NavGraphBuilder.menu(navController: NavHostController) {
    composable(menuRoute) {
        MenuListScreen(
            products = sampleProducts,
            onNavigateToDetails = {
                navController.navigateToProductDetails(it.id)
            })
    }
}

fun NavController.navigateToMenu(navOptions: NavOptions? = null) {
    navigate(menuRoute, navOptions)
}