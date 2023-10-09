package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.AuthenticationScreen

internal const val authenticationRoute = "authentication"
fun NavGraphBuilder.authentication(
    onEnterUserClick: (String) -> Unit,
) {
    // composable é a função onde configuramos uma rota de navegação
    composable(authenticationRoute) {
        AuthenticationScreen(
            onEnterUserClick = onEnterUserClick
        )
    }
}

fun NavController.navigateToAuthentication() {
    navigate(authenticationRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }
}