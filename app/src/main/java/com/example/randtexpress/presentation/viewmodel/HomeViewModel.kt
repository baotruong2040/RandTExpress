package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.CartRepository
import com.example.randtexpress.domain.repository.CategoryRepository
import com.example.randtexpress.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeCategorySection(
    val id: Int,
    val name: String,
    val products: List<ProductResponse>
)

data class HomeUiState(
    val isLoading: Boolean = false,
    val categorySections: List<HomeCategorySection> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
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
                val categories = categoryRepository.getCategories()
                    .categories
                    .sortedBy { it.id }
                val products = productRepository.getProducts(categoryId = null, page = 1, pageSize = 50).products
                val productsByCategoryId = products.groupBy { it.categoryId }

                val sections = categories.map { category ->
                    HomeCategorySection(
                        id = category.id,
                        name = category.name ?: "Danh mục",
                        products = productsByCategoryId[category.id].orEmpty().take(5)
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categorySections = sections
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
                name = product.name ?: "",
                imageUrl = product.imageUrl,
                price = product.price ?: 0L
            )
        }
    }
}
