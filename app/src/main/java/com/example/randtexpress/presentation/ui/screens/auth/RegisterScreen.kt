package com.example.randtexpress.presentation.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randtexpress.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.registerUiState.collectAsState()
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
            ) {
                AuthTabs(
                    selectedTab = AuthTab.Register,
                    onLoginClick = onLoginClick,
                    onRegisterClick = {}
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AuthTextField(
                        value = uiState.username,
                        onValueChange = viewModel::onRegisterUsernameChange,
                        placeholder = "Tên đăng nhập",
                        leadingIcon = Icons.Default.Person
                    )
                    AuthPasswordField(
                        value = uiState.password,
                        onValueChange = viewModel::onRegisterPasswordChange,
                        placeholder = "Mật khẩu",
                        passwordVisible = passwordVisible,
                        onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                        leadingIcon = Icons.Default.Lock
                    )
                    AuthTextField(
                        value = uiState.fullName,
                        onValueChange = viewModel::onRegisterFullNameChange,
                        placeholder = "Họ và tên",
                        leadingIcon = Icons.Default.Person
                    )
                    AuthTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onRegisterEmailChange,
                        placeholder = "Email",
                        leadingIcon = Icons.Default.Email
                    )
                    AuthTextField(
                        value = uiState.phone,
                        onValueChange = viewModel::onRegisterPhoneChange,
                        placeholder = "Số điện thoại",
                        leadingIcon = Icons.Default.Phone
                    )
                    AuthTextField(
                        value = uiState.address,
                        onValueChange = viewModel::onRegisterAddressChange,
                        placeholder = "Địa chỉ (không bắt buộc)",
                        leadingIcon = Icons.Default.Home
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AuthErrorMessage(message = uiState.errorMessage)
                    AuthPrimaryButton(
                        text = "Đăng ký",
                        isLoading = uiState.isLoading,
                        onClick = {
                            scope.launch {
                                if (viewModel.register()) {
                                    onLoginClick()
                                }
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
