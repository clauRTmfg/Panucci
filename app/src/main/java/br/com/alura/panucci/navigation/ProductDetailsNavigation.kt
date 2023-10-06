package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel

// não é bem explicado no curso o uso de "internal",
// é um modificador que restringe mais  o uso da variável
private const val productDetailsRoute = "productDetails"
internal const val productIdArg = "productId"
internal const val promoCodeArg = "promoCode"


// alura://panucci.com.br/productDetails/9adccd9a-3918-4996-8c96-2f5b9143cef2?promoCode=PANNUCI10

fun NavGraphBuilder.productDetails(
    onNavigateToCheckout: () -> Unit,
    onPopBackStack: () -> Unit
) {
    composable(
        "$productDetailsRoute/{$productIdArg}",
        deepLinks = listOf(navDeepLink { uriPattern = "$uri/$productDetailsRoute/{$productIdArg}?$promoCodeArg={$promoCodeArg}" })
    ) {
        it.arguments?.getString(productIdArg)?.let { id ->
            val viewModel =
                viewModel<ProductDetailsViewModel>(factory = ProductDetailsViewModel.Factory)
            val uiState by viewModel.uiState.collectAsState()
            ProductDetailsScreen(
                uiState = uiState,
                onOrderClick = onNavigateToCheckout,
                onSearchAgain = { viewModel.findProductById(id) },
                onBackClick = onPopBackStack
            )
        } ?: LaunchedEffect(Unit) {
            onPopBackStack()
        }
    }
}

fun NavController.navigateToProductDetails(id: String) {
    navigate("$productDetailsRoute/$id")
}