package br.com.alura.panucci.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.model.Product
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.ui.viewmodels.HighlightsListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

internal const val highlightsRoute = "highlights"

fun NavGraphBuilder.highlightsList(
    context: Context,
    onNavigateToCheckout: () -> Unit,
    onNavigateToProductDetails: (Product) -> Unit,
    login: () -> Unit
) {
    // composable é a função onde configuramos uma rota de navegação
    composable(highlightsRoute) {
        val viewModel = viewModel<HighlightsListViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val userPreferences = stringPreferencesKey("usuario_logado")
        var user: String? by remember {
            mutableStateOf(null)
        }

        var loginState by remember {
            mutableStateOf("verifying")
        }


        LaunchedEffect(null) {
            user = context.dataStore.data.first()[userPreferences]
            loginState = "verified"
        }

        when (loginState) {
            "verifying" -> {
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

            "verified" -> {
                user?.let {
                    HighlightsListScreen(
                        uiState = uiState,
                        onProductClick = onNavigateToProductDetails,
                        onOrderClick = onNavigateToCheckout,
                        )
                } ?: LaunchedEffect(null) {
                    login()
                }
            }
        }
    }
}

fun NavController.navigateToHighlightsList(navOptions: NavOptions? = null) {
    navigate(highlightsRoute, navOptions)
}
