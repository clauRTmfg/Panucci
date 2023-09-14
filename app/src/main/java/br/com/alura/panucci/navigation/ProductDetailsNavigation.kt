package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel

internal const val productDetailsRoute = "productDetails"
private const val productIdArg = "productId"

fun NavGraphBuilder.productDetails(
    onNavigateToCheckout: () -> Unit,
    onPopBackStack: () -> Unit
) {
    composable("$productDetailsRoute/{$productIdArg}")
    {
        it.arguments?.getString(productIdArg)?.let { id ->
            val viewModel = viewModel<ProductDetailsViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.findProductById(id)
            }
            ProductDetailsScreen(
                uiState = uiState,
                onOrderClick = onNavigateToCheckout,
                onSearchAgain = { viewModel.findProductById(id) },
                onBackClick = onPopBackStack)
        } ?: LaunchedEffect(Unit) {
            onPopBackStack()
        }
    }
}

fun NavController.navigateToProductDetails(id: String) {
    navigate("$productDetailsRoute/$id")
}