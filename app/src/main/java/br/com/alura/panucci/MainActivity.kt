package br.com.alura.panucci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToAuthentication
import br.com.alura.panucci.navigation.navigateToBottomAppBarItemSelected
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.preferences.userPreferences
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.components.bottomAppBarItems
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PanucciApp()
                }
            }
        }
    }

}

@Composable
fun PanucciApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // LaunchedEffect (coroutine): este escopo não é reexecutado na recomposição .
    // Usamos o addOnDestinationChangedListener/backQueue para termos a lista
    // de rotas clicadas
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, _, _ ->
            navController.currentBackStack.value.map {
                it.destination.route
            }
        }
    }

    val backStackEntryState by navController.currentBackStackEntryAsState()
    val orderDoneMsg = backStackEntryState
        ?.savedStateHandle
        ?.getStateFlow<String?>("order_done", null)
        ?.collectAsState()
    //backStackEntryState?.savedStateHandle?.remove<String?>("order_done")

    // aqui o codigo que traz o item atual selecionado
    val currentDestination = backStackEntryState?.destination

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    orderDoneMsg?.value?.let {
        scope.launch {
            snackbarHostState.showSnackbar(message = it)
        }
    }

    val currentRoute = currentDestination?.route
    val selectedItem by remember(currentDestination) {
        val item = when (currentRoute) {
            highlightsRoute -> BottomAppBarItem.HighLightsList
            menuRoute -> BottomAppBarItem.Menu
            drinksRoute -> BottomAppBarItem.Drinks
            else -> BottomAppBarItem.HighLightsList
        }
        mutableStateOf(item)
    }

    val containsInBottomAppBarItems = when (currentRoute) {
        highlightsRoute, menuRoute, drinksRoute -> true
        else -> false
    }

    val showFab = when (currentDestination?.route) {
        menuRoute, drinksRoute -> true
        else -> false
    }

    PanucciApp(
        snackbarHostState = snackbarHostState,
        bottomAppBarItemSelected = selectedItem,
        onBottomAppBarItemSelectedChange = { item ->
            navController.navigateToBottomAppBarItemSelected(item)
        },
        onFabClick = {
            navController.navigateToCheckout()
        },
        showTopBar = containsInBottomAppBarItems,
        showBottomBar = containsInBottomAppBarItems,
        showFab = showFab,
        onLogout = {
            scope.launch {
                context.dataStore.edit {
                    it.remove(userPreferences)
                }
            }
            navController.navigateToAuthentication()
        }
    ) {

        PanucciNavHost(
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    showTopBar: Boolean = false,
    showBottomBar: Boolean = false,
    showFab: Boolean = false,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    // importante : o content de um Scaffold precisa ser o último parâmetro
    content: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(Modifier.padding(8.dp)) {
                    Text(text = it.visuals.message,
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }
            }
        },
        topBar = {
            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                    actions = {
                        IconButton(
                            onClick = onLogout,
                            Modifier.semantics {
                                contentDescription = "botao logout"
                            }) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "sair do app"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = onFabClick,
                    Modifier.semantics {
                        contentDescription = "botao pedir principal"
                    }
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp(content = {})
        }
    }
}