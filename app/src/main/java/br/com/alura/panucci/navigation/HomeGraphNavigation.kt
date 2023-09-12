package br.com.alura.panucci.navigation

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.ui.components.BottomAppBarItem

internal const val homeGraphRoute = "home"

fun NavGraphBuilder.homeGraph(
    context: Context,
    navController: NavHostController
) {
    navigation(startDestination = highlightsRoute, route = homeGraphRoute) {
        highlightsList(context, navController)
        menu(navController)
        drinks(navController)
    }
}

fun NavController.navigateToHomeGraph() {
    navigate(homeGraphRoute)
}

fun NavController.navigateToBottomAppBarItemSelected(
    item: BottomAppBarItem
) {
    val (route, navigateToItem) = when (item) {
        BottomAppBarItem.Drinks -> Pair(
            drinksRoute,
            ::navigateToDrinks
        )

        BottomAppBarItem.HighLightsList -> Pair(
            highlightsRoute,
            ::navigateToHighlightsList
        )

        BottomAppBarItem.Menu -> Pair(
            menuRoute,
            ::navigateToMenu
        )
    }

    // aqui usamos estas duas opções pra garantir que na backstack
    // haja apenas uma instancia de cada destino clicado.
    // launchSingleTop é para que uma tela não seja recarregada caso
    // seja clicada várias vezes.
    // popUpTo remove da backstack todos itens acima da rota em questão
    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }

    navigateToItem(navOptions)
}