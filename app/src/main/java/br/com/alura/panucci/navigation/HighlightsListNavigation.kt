package br.com.alura.panucci.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import kotlinx.coroutines.flow.first

internal const val highlightsRoute = "highlights"
fun NavGraphBuilder.highlightsList(
    context: Context,
    navController: NavHostController
) {
    // composable é a função onde configuramos uma rota de navegação
    composable(highlightsRoute) {
        val userPreferences = stringPreferencesKey("usuario_logado")
        var user: String? by remember {
            mutableStateOf(null)
        }
        var dataState by remember {
            mutableStateOf("loading")
        }
        LaunchedEffect(null) {
            user = context.dataStore.data.first()[userPreferences]
            dataState = "finished"
        }

        when (dataState) {
            "loading" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Carregando...",
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }

            "finished" -> {
                user?.let {
                    HighlightsListScreen(
                        products = sampleProducts,
                        onNavigateToDetails = {
                            // navigate é a função que efetua a navegação
                            navController.navigateToProductDetails(it.id)
                        },
                        onNavigateToCheckout = {
                            navController.navigateToCheckout()
                        })
                } ?: LaunchedEffect(null) {
                    navController.navigateToAuthentication()
                }
            }
        }
    }
}

fun NavController.navigateToHighlightsList(navOptions: NavOptions? = null) {
    navigate(highlightsRoute, navOptions)
}
