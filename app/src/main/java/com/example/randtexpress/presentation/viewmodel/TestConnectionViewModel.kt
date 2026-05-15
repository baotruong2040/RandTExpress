package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.domain.repository.AuthRepository
import com.example.randtexpress.domain.repository.CategoryRepository
import com.example.randtexpress.domain.repository.NotificationRepository
import com.example.randtexpress.domain.repository.OrderRepository
import com.example.randtexpress.domain.repository.ProductRepository
import com.example.randtexpress.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestResult(
    val testName: String,
    val isSuccess: Boolean,
    val message: String,
    val data: String? = null
)

@HiltViewModel
class TestConnectionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val orderRepository: OrderRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _testResults = MutableStateFlow<List<TestResult>>(emptyList())
    val testResults: StateFlow<List<TestResult>> = _testResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun testLogin() = runTest("Login") {
        val result = authRepository.login("admin", "change_admin_password")
        "Token: ${result.token?.take(20) ?: "no token"}... | User: ${result.userId} | Role: ${result.role}"
    }

    fun testRegister() = runTest("Register") {
        val result = authRepository.register(
            username = "test_user_${System.currentTimeMillis()}",
            password = "password123",
            fullName = "Test User",
            email = "test_${System.currentTimeMillis()}@example.com",
            phone = "0912345678"
        )
        "User: ${result.userId} | Role: ${result.role} | Token: ${result.token?.take(20) ?: "no token"}..."
    }

    fun testGetProducts() = runTest("Get Products") {
        val result = productRepository.getProducts(page = 1, pageSize = 5)
        "Found ${result.products.size} products | Total: ${result.pagination.total}"
    }

    fun testGetCategories() = runTest("Get Categories") {
        val result = categoryRepository.getCategories()
        "Found ${result.categories.size} categories"
    }

    fun testCreateOrder() = runTest("Create Order") {
        val items = listOf(
            Pair(1, 2),
            Pair(2, 1)
        )
        val result = orderRepository.createOrder(
            deliveryAddress = "123 Test Street",
            items = items
        )
        "Order: ${result.orderId} | Amount: ${result.totalAmount} | Status: ${result.status}"
    }

    fun testGetNotifications() = runTest("Get Notifications") {
        val result = notificationRepository.getNotifications(pageSize = 5)
        "Found ${result.notifications.size} notifications | Unread: ${result.pagination.unreadCount}"
    }

    fun testMarkNotificationRead() = runTest("Mark Notification Read") {
        try {
            val notifications = notificationRepository.getNotifications(pageSize = 1)
            if (notifications.notifications.isNotEmpty()) {
                val result = notificationRepository.markAsRead(notifications.notifications[0].id)
                "Notification ${result.id} marked as read: ${result.isRead}"
            } else {
                "No notifications available to mark as read"
            }
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    fun testGetUsers() = runTest("Get Users") {
        val result = userRepository.getUsers(pageSize = 5)
        "Found ${result.users.size} users | Total: ${result.pagination.total}"
    }

    fun clearResults() {
        _testResults.value = emptyList()
    }

    private fun runTest(testName: String, block: suspend () -> String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = block()
                _testResults.value = _testResults.value + TestResult(
                    testName = testName,
                    isSuccess = true,
                    message = "Success",
                    data = data
                )
            } catch (e: Exception) {
                _testResults.value = _testResults.value + TestResult(
                    testName = testName,
                    isSuccess = false,
                    message = e.message ?: "Unknown error",
                    data = null
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
