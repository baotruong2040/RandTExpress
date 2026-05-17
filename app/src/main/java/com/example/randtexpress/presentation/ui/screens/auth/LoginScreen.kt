package com.example.randtexpress.presentation.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randtexpress.R
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red) // Background dark, can be an Image if available
    ) {
        // Example background image placeholder logic (using a dark color for now)
        // Image(painter = painterResource(id = R.drawable.login_bg), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())

        // Top Logo Banner
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(140.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(Color(0xFFD32F2F)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Placeholder for McDonald's logo style
                Text(
                    text = "M",
                    color = Color(0xFFFFC107),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "RT Express",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Main Login Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.DarkGray.copy(alpha = 0.6f))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Sign in / Sign up tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFD32F2F))
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Sign in", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50.dp))
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButtonWithNavigation(
                                text = "Sign up",
                                onClick = onRegisterClick
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Username Input
                    TextField(
                        value = uiState.username,
                        onValueChange = viewModel::onLoginUsernameChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Username or Email", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password Input
                    TextField(
                        value = uiState.password,
                        onValueChange = viewModel::onLoginPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Password", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                    ),
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Forgot Password
                    Text(
                        text = "Forgot password ?",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Login Button
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.login()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(50.dp),
                        border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(50.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFFD32F2F),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Or Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                        Text(
                            text = "or",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Social Login Buttons (Placeholders)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialIconButton(iconColor = Color(0xFF1877F2), text = "f") // Facebook
                        SocialIconButton(iconColor = Color.Black, text = "") // Apple
                        SocialIconButton(iconColor = Color(0xFFEA4335), text = "G") // Google
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
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

@Composable
fun TextButtonWithNavigation(text: String, onClick: () -> Unit) {
    androidx.compose.material3.TextButton(onClick = onClick) {
        Text(text = text, color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SocialIconButton(iconColor: Color, text: String) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50.dp))
                .background(if (text == "") Color.Black else Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (text == "f") {
                 Icon(
                     painter = painterResource(id = android.R.drawable.ic_menu_info_details), // Placeholder
                     contentDescription = null,
                     tint = Color(0xFF1877F2),
                     modifier = Modifier.size(30.dp)
                 )
            } else if (text == "G") {
                Text(text = "G", color = Color(0xFFEA4335), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            } else {
                Text(text = text, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            }
        }
    }
}
