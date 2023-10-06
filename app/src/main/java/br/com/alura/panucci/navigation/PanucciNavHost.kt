package br.com.alura.panucci.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.ui.components.BottomAppBarItem
import kotlinx.coroutines.CoroutineScope

internal const val uri = "alura://panucci.com.br"
@Composable
fun PanucciNavHost(navController: NavHostController, context: Context, scope: CoroutineScope) {

    // NavHost é responsável por configurar o gráfico de navegação
    NavHost(
        navController = navController,
        startDestination = homeGraphRoute
    ) {
        homeGraph(
            context,
            onNavigateToCheckout = { navController.navigateToCheckout() },
            onNavigateToProductDetails = { product ->
                navController.navigateToProductDetails(product.id)
            },
            login = { navController.navigateToAuthentication() }
        )
        productDetails(
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
            onPopBackStack = {
                navController.navigateUp()
            },
        )
        checkout(
            onPopBackStack = {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("order_done", "Pedido realizado com sucesso 👍😊")
                navController.navigateUp()
            },
        )

        authentication(
            scope,
            context,
            navigateToHighlightsList = {
                navController.navigateToHighlightsList(navOptions {
                    popUpTo(navController.graph.id)
                })
            }
        )
    }
}


