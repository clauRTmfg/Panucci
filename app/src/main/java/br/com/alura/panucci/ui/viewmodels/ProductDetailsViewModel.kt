package br.com.alura.panucci.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductDetailsViewModel(
    private val dao: ProductDao = ProductDao()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String) {
        _uiState.update { ProductDetailsUiState.Loading }
        val dataState = dao.findById(id)?.let { product ->
            ProductDetailsUiState.Success(product = product)
        } ?: ProductDetailsUiState.Failure
        _uiState.update { dataState }
    }

}