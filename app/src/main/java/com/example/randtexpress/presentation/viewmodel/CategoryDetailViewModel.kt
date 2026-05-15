package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryDetailUiState(
    val isLoading: Boolean = false,
    val products: List<ProductResponse> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val pageSize: Int = 20,
    val hasMoreProducts: Boolean = true
)

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    fun loadProductsByCategory(categoryName: String, page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = productRepository.getProducts(
                    categoryId = null,
                    page = page,
                    pageSize = 20
                )
                
                val filteredProducts = response.products.filter { 
                    it.categoryName == categoryName 
                }

                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        products = filteredProducts,
                        currentPage = page,
                        hasMoreProducts = response.pagination.total > filteredProducts.size
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loadNextPage(categoryName: String) {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMoreProducts) {
            loadProductsByCategory(categoryName, currentState.currentPage + 1)
        }
    }
}
