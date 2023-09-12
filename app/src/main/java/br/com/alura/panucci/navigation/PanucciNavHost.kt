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

@Composable
fun PanucciNavHost(navController: NavHostController, context: Context, scope: CoroutineScope) {

    // NavHost é responsável por configurar o gráfico de navegação
    NavHost(
        navController = navController,
        startDestination = homeGraphRoute
    ) {
        homeGraph(context, navController)
        authentication(scope, context, navController)
        productDetails(navController)
        checkout(navController)
    }
}


