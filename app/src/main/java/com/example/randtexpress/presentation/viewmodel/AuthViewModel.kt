package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.preferences.UserPreferences
import com.example.randtexpress.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    fun onLoginUsernameChange(value: String) {
        _loginUiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun onLoginPasswordChange(value: String) {
        _loginUiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onRegisterUsernameChange(value: String) {
        _registerUiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun onRegisterPasswordChange(value: String) {
        _registerUiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onRegisterFullNameChange(value: String) {
        _registerUiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onRegisterEmailChange(value: String) {
        _registerUiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onRegisterPhoneChange(value: String) {
        _registerUiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun onRegisterAddressChange(value: String) {
        _registerUiState.update { it.copy(address = value, errorMessage = null) }
    }

    suspend fun login(): Boolean {
        // Login only saves the token when the backend returns one.
        val currentState = loginUiState.value
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _loginUiState.update { it.copy(errorMessage = "Vui lòng nhập username và mật khẩu.") }
            return false
        }

        _loginUiState.update { it.copy(isLoading = true, errorMessage = null) }
        return try {
            val response = authRepository.login(
                username = currentState.username.trim(),
                password = currentState.password
            )
            val token = response.token
            if (token.isNullOrBlank()) {
                throw IllegalStateException("Backend chưa trả về token đăng nhập.")
            }
            userPreferences.saveSession(
                token = token,
                userId = response.userId,
                role = response.role
            )
            true
        } catch (e: Exception) {
            _loginUiState.update { it.copy(errorMessage = e.message ?: "Đăng nhập thất bại.") }
            false
        } finally {
            _loginUiState.update { it.copy(isLoading = false) }
        }
    }

    suspend fun register(): Boolean {
        // Register just creates the account; it does not persist session data.
        val currentState = registerUiState.value
        if (
            currentState.username.isBlank() ||
            currentState.password.isBlank() ||
            currentState.fullName.isBlank() ||
            currentState.email.isBlank() ||
            currentState.phone.isBlank()
        ) {
            _registerUiState.update { it.copy(errorMessage = "Vui lòng điền đầy đủ các trường bắt buộc.") }
            return false
        }

        if (currentState.password.length < 8) {
            _registerUiState.update { it.copy(errorMessage = "Mật khẩu phải có ít nhất 8 ký tự.") }
            return false
        }

        _registerUiState.update { it.copy(isLoading = true, errorMessage = null) }
        return try {
            authRepository.register(
                username = currentState.username.trim(),
                password = currentState.password,
                fullName = currentState.fullName.trim(),
                email = currentState.email.trim(),
                phone = currentState.phone.trim(),
                address = currentState.address.trim().ifBlank { null }
            )
            true
        } catch (e: Exception) {
            _registerUiState.update { it.copy(errorMessage = e.message ?: "Đăng kí thất bại.") }
            false
        } finally {
            _registerUiState.update { it.copy(isLoading = false) }
        }
    }
}
