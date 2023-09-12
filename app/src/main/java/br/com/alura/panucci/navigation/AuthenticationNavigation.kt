package br.com.alura.panucci.navigation

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.preferences.userPreferences
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal const val authenticationRoute = "authentication"
fun NavGraphBuilder.authentication(
    scope: CoroutineScope,
    context: Context,
    navController: NavHostController
) {
    // composable é a função onde configuramos uma rota de navegação
    composable(authenticationRoute) {
        AuthenticationScreen(
            onEnterClick = { user ->
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

fun NavController.navigateToAuthentication() {
    navigate(authenticationRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }
}