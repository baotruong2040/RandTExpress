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

    fun testLogin() = runTest("Đăng nhập") {
        val result = authRepository.login("admin", "change_admin_password")
        "Token: ${result.token?.take(20) ?: "không có token"}... | Người dùng: ${result.userId} | Vai trò: ${result.role}"
    }

    fun testRegister() = runTest("Đăng ký") {
        val result = authRepository.register(
            username = "test_user_${System.currentTimeMillis()}",
            password = "password123",
            fullName = "Test User",
            email = "test_${System.currentTimeMillis()}@example.com",
            phone = "0912345678"
        )
        "Người dùng: ${result.userId} | Vai trò: ${result.role} | Token: ${result.token?.take(20) ?: "không có token"}..."
    }

    fun testGetProducts() = runTest("Lấy sản phẩm") {
        val result = productRepository.getProducts(page = 1, pageSize = 5)
        "Tìm thấy ${result.products.size} sản phẩm | Tổng: ${result.pagination.total}"
    }

    fun testGetCategories() = runTest("Lấy danh mục") {
        val result = categoryRepository.getCategories()
        "Tìm thấy ${result.categories.size} danh mục"
    }

    fun testCreateOrder() = runTest("Tạo đơn hàng") {
        val items = listOf(
            Pair(1, 2),
            Pair(2, 1)
        )
        val result = orderRepository.createOrder(
            deliveryAddress = "123 Test Street",
            items = items
        )
        "Đơn hàng: ${result.orderId} | Số tiền: ${result.totalAmount} | Trạng thái: ${result.status}"
    }

    fun testGetNotifications() = runTest("Lấy thông báo") {
        val result = notificationRepository.getNotifications(pageSize = 5)
        "Tìm thấy ${result.notifications.size} thông báo | Chưa đọc: ${result.pagination.unreadCount}"
    }

    fun testMarkNotificationRead() = runTest("Đánh dấu đã đọc") {
        try {
            val notifications = notificationRepository.getNotifications(pageSize = 1)
            if (notifications.notifications.isNotEmpty()) {
                val result = notificationRepository.markAsRead(notifications.notifications[0].id)
                "Thông báo ${result.id} đã đánh dấu đọc: ${result.isRead}"
            } else {
                "Không có thông báo để đánh dấu đã đọc"
            }
        } catch (e: Exception) {
            throw Exception("Lỗi: ${e.message}")
        }
    }

    fun testGetUsers() = runTest("Lấy người dùng") {
        val result = userRepository.getUsers(pageSize = 5)
        "Tìm thấy ${result.users.size} người dùng | Tổng: ${result.pagination.total}"
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
                    message = "Thành công",
                    data = data
                )
            } catch (e: Exception) {
                _testResults.value = _testResults.value + TestResult(
                    testName = testName,
                    isSuccess = false,
                    message = e.message ?: "Lỗi không xác định",
                    data = null
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
