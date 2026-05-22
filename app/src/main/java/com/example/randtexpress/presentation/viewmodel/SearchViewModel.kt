package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.CartRepository
import com.example.randtexpress.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<ProductResponse> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value, errorMessage = null) }
        searchJob?.cancel()

        if (value.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(350)
            performSearch(value.trim())
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val response = productRepository.searchProducts(
                query = query,
                page = 1,
                pageSize = 20
            )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    results = response.products
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Không thể tìm kiếm"
                )
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
