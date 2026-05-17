package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.CartRepository
import com.example.randtexpress.domain.repository.ProductRepository
import com.example.randtexpress.presentation.ui.FixedCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val categoryProducts: Map<String, List<ProductResponse>> = emptyMap(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = productRepository.getProducts(categoryId = null, page = 1, pageSize = 50)

                val groupedByCategory = FixedCategories.orderedCategories.associateWith { category ->
                    response.products
                        .filter { product ->
                            FixedCategories.mapRawCategoryToFixed(product.categoryName) == category
                        }
                        .take(5)
                }

                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        categoryProducts = groupedByCategory
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun addToCart(product: ProductResponse) {
        viewModelScope.launch {
            cartRepository.addToCart(
                productId = product.id,
                name = product.name,
                imageUrl = product.imageUrl,
                price = product.price
            )
        }
    }
}
