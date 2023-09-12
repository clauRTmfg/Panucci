package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.ProductDetailsScreen

internal const val productDetailsRoute = "productDetails"
private const val productIdArg = "productId"

fun NavGraphBuilder.productDetails(navController: NavHostController) {
    composable("$productDetailsRoute/{$productIdArg}")
    { backStackEntry ->
        val id = backStackEntry.arguments?.getString(productIdArg)
        sampleProducts.find {
            it.id == id
        }?.let {
            ProductDetailsScreen(
                product = it,
                onNavigateToCheckout = {
                    navController.navigateToCheckout()
                })
        } ?: LaunchedEffect(Unit) {
            navController.navigateUp()
        }
    }
}

fun NavController.navigateToProductDetails(id: String) {
    navigate("$productDetailsRoute/$id")
}