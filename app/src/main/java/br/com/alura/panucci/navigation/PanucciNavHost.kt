package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.preferences.userPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal const val uri = "alura://panucci.com.br"

@Composable
fun PanucciNavHost(
    navController: NavHostController
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
            onEnterUserClick = { user ->
                scope.launch {
                    context.dataStore.edit {
                        it[userPreferences] = user
                    }
                }
                navController.navigateToHighlightsList(navOptions {
                    popUpTo(navController.graph.id)
                })
            }
        )
    }
}


