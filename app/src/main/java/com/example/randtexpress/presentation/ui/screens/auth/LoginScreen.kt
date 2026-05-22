package com.example.randtexpress.presentation.ui.screens.auth

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randtexpress.presentation.ui.components.ExitConfirmDialog
import com.example.randtexpress.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onExitApp: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.loginUiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showExitDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
            ) {
                AuthTabs(
                    selectedTab = AuthTab.Login,
                    onLoginClick = {},
                    onRegisterClick = onRegisterClick
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AuthTextField(
                        value = uiState.username,
                        onValueChange = viewModel::onLoginUsernameChange,
                        placeholder = "Tên đăng nhập hoặc Email",
                        leadingIcon = Icons.Default.Person
                    )
                    AuthPasswordField(
                        value = uiState.password,
                        onValueChange = viewModel::onLoginPasswordChange,
                        placeholder = "Mật khẩu",
                        passwordVisible = passwordVisible,
                        onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                        leadingIcon = Icons.Default.Lock
                    )
                    Text(
                        text = "Quên mật khẩu?",
                        modifier = Modifier.fillMaxWidth(),
                        color = AuthDesign.Secondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AuthPrimaryButton(
                        text = "Đăng nhập",
                        isLoading = uiState.isLoading,
                        onClick = {
                            scope.launch {
                                viewModel.login()
                            }
                        }
                    )
                    AuthErrorMessage(message = uiState.errorMessage)
                }

                AuthSocialSection()
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showExitDialog) {
        ExitConfirmDialog(
            onConfirmExit = {
                showExitDialog = false
                onExitApp()
            },
            onDismiss = { showExitDialog = false }
        )
    }
}
