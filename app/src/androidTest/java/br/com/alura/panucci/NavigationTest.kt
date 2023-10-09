package br.com.alura.panucci

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.authenticationRoute
import br.com.alura.panucci.navigation.checkoutRoute
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToProductDetails
import br.com.alura.panucci.navigation.productDetailsRoute
import br.com.alura.panucci.navigation.productIdArg
import br.com.alura.panucci.sampledata.sampleProducts
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PanucciApp(navController = navController)
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        // aqui é exibida toda a informação de nós da tela
        composeTestRule.onRoot().printToLog("Panucci Nodes")

        composeTestRule
            .onNodeWithText("Welcome to Panucci")
            .assertIsDisplayed()
    }

    @Test
    fun appNavHost_testa_Login() {
        composeTestRule
            .onNodeWithText("User")
            .performTextInput("clau")
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("0000")
        composeTestRule
            .onNodeWithText("Enter")
            .performClick()

        composeTestRule
            .onNodeWithText("User")
            .performTextInput("clau")
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("0000")
        composeTestRule
            .onNodeWithText("Enter")
            .performClick()


    }

    @Test
    fun appNavHost_testa_exibicao_de_HighlightsListScreen_apos_login() {

        appNavHost_testa_Login()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, highlightsRoute)

        composeTestRule
            .onNodeWithText("Destaques do dia")
            .assertIsDisplayed()
    }

    @Test
    fun appNavHost_testa_exibicao_de_MenuScreen() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Menu")
            .performClick()

        composeTestRule
            .onAllNodesWithText("Menu")
            .assertCountEquals(2)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, menuRoute)
    }

    @Test
    fun appNavHost_testa_exibicao_de_DrinksScreen() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Bebidas")
            .performClick()

        composeTestRule
            .onAllNodesWithText("Bebidas")
            .assertCountEquals(2)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, drinksRoute)
    }

    @Test
    fun appNavHost_testa_exibicao_de_HighlightsScreen_por_click() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Bebidas")
            .performClick()

        composeTestRule
            .onNodeWithText("Destaques")
            .performClick()

        composeTestRule
            .onNodeWithText("Destaques do dia")
            .assertIsDisplayed()
    }


    @Test
    fun appNavHost_testa_exibicao_de_ProductDetailsScreen_apartirde_Highlights() {

        // aqui estamos considerando falha na busca do produto

        appNavHost_testa_Login()

        composeTestRule
            .onAllNodesWithContentDescription("Item de produto destaque")
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(3000) {
            composeTestRule
                .onAllNodesWithText("Falha ao buscar os detalhes do produto")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithText("Falha ao buscar os detalhes do produto")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Buscar novamente")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{$productIdArg}")

        composeTestRule
            .onNodeWithContentDescription("tela de detalhes com sucesso")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Falha ao buscar os detalhes do produto")
            .assertDoesNotExist()

    }

    @Test
    fun appNavHost_testa_exibicao_de_ProductDetailsScreen_apartirde_Menu() {

        // aqui não supomos falha na busca do produto

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Menu")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Item de produto de menu")
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(3000) {
            composeTestRule
                .onAllNodesWithContentDescription("tela de detalhes com sucesso")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithContentDescription("tela de detalhes com sucesso")
            .assertIsDisplayed()

    }

    @Test
    fun appNavHost_testa_exibicao_de_ProductDetailsScreen_apartirde_Drinks() {

        // aqui não supomos falha na busca do produto

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Bebidas")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Item de produto de bebidas")
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(3000) {
            composeTestRule
                .onAllNodesWithContentDescription("tela de detalhes com sucesso")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithContentDescription("tela de detalhes com sucesso")
            .assertIsDisplayed()

    }

    @Test
    fun appNavHost_testa_checkout_de_highlights() {

        appNavHost_testa_Login()

        composeTestRule
            .onAllNodesWithContentDescription("botao pedir dos itens de highlights")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)

    }

    @Test
    fun appNavHost_testa_checkout_de_menu() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Menu")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("botao pedir principal")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)

    }

    @Test
    fun appNavHost_testa_checkout_de_drinks() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Bebidas")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("botao pedir principal")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)

    }

    @Test
    fun appNavHost_testa_checkout_de_productDetails() {

        appNavHost_testa_Login()

        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.first().id)
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule
                .onAllNodesWithContentDescription("tela de detalhes com sucesso")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithText("Pedir")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)

    }

    @Test
    fun appNavHost_testa_pedir_apartirde_checkout() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithText("Bebidas")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("botao pedir principal")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("botao pedir final")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, drinksRoute)

    }

    @Test
    fun appNavHost_testa_botao_logout() {

        appNavHost_testa_Login()

        composeTestRule
            .onNodeWithContentDescription("botao logout")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, authenticationRoute)

    }

}