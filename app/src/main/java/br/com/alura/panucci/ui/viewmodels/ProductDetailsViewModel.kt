package br.com.alura.panucci.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.navigation.productIdArg
import br.com.alura.panucci.navigation.promoCodeArg
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ProductDetailsViewModel(
    private val dao: ProductDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // neste caso utilizamos generics no MutableStateFlow porque temos várias sinalizações
    // para o uiState (explicação dada pela Alura)
    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var discount = BigDecimal.ZERO

    init {
        viewModelScope.launch {
            val promoCode = savedStateHandle.get<String?>(promoCodeArg)
            discount = when (promoCode) {
                "PANUCCI10" -> BigDecimal("0.1")
                else -> BigDecimal.ZERO
            }
            delay(2000)
            savedStateHandle.getStateFlow<String?>(productIdArg, null)
                .filterNotNull()
                .collect {id ->
                    findProductById("0")
                    //findProductById(id)
                }
        }
    }

    fun findProductById(id: String) {
        _uiState.update { ProductDetailsUiState.Loading }
        val dataState = dao.findById(id)?.let { product ->
            val priceWithDiscount = product.price - (product.price * discount)
            ProductDetailsUiState.Success(product = product.copy(price = priceWithDiscount))
        } ?: ProductDetailsUiState.Failure
        _uiState.update { dataState }
    }

    // aqui uma implementação necessária para que o savedStateHandle
    // receba o id do produto (usado na findProductById)
    // no momento que a ProductDetailsViewModel é criada.
    // Anteriormente a chamada de findProductById era feita em ProductDetailsNavigation,
    // antes da chamada de ProductDetailsScreen.
    // A estrutura abaixo é proposta pela documentação, como maneira de se criar um
    // viewmodel com suas dependências necessárias.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProductDetailsViewModel(
                    dao = ProductDao(),
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }

}