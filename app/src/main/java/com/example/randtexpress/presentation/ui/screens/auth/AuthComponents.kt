package com.example.randtexpress.presentation.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randtexpress.R

internal object AuthDesign {
    val Primary = Color(0xFFBD0D00)
    val PrimaryContainer = Color(0xFFE32D19)
    val Secondary = Color(0xFF8F4E00)
    val Surface = Color(0xFFFCF9F8)
    val SurfaceContainer = Color(0xFFFEE6D1)
    val SurfaceVariant = Color(0xFFE9DAD0)
    val OnSurface = Color(0xFF1C1B1B)
    val OnSurfaceVariant = Color(0xFF7E7169)
    val OutlineVariant = Color(0xFFE5D5C9)
    val Error = Color(0xFFBA1A1A)
    val CardBackground = Color(0xFFFEE6D1).copy(alpha = 0.9f)
    val FieldBackground = Color(0xFFF9F5F2)
    val CardShape = RoundedCornerShape(40.dp)
    val FieldShape = RoundedCornerShape(16.dp)
    val PillShape = RoundedCornerShape(999.dp)
}

@Composable
internal fun AuthBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFBD0D00),
                        Color(0xFFE32D19),
                        Color(0xFFFFA726),
                        Color(0xFFFFD54F)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.16f),
                            Color.Transparent,
                            AuthDesign.Primary.copy(alpha = 0.16f)
                        )
                    )
                )
        )
        content()
    }
}

@Composable
internal fun AuthGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .shadow(18.dp, AuthDesign.CardShape)
            .clip(AuthDesign.CardShape)
            .background(AuthDesign.CardBackground)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.56f)), AuthDesign.CardShape)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "R&T Express",
            color = AuthDesign.Primary,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        content()
    }
}

@Composable
internal fun AuthTabs(
    selectedTab: AuthTab,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(AuthDesign.PillShape)
            .background(AuthDesign.SurfaceVariant.copy(alpha = 0.72f))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuthTabButton(
            text = "Đăng nhập",
            selected = selectedTab == AuthTab.Login,
            onClick = onLoginClick,
            modifier = Modifier.weight(1f)
        )
        AuthTabButton(
            text = "Đăng ký",
            selected = selectedTab == AuthTab.Register,
            onClick = onRegisterClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = if (selected) AuthDesign.Primary else AuthDesign.OnSurfaceVariant.copy(alpha = 0.72f)
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .clip(AuthDesign.PillShape)
            .background(if (selected) Color.White else Color.Transparent),
        shape = AuthDesign.PillShape,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(8.dp, AuthDesign.FieldShape, clip = false)
            .clip(AuthDesign.FieldShape)
            .background(AuthDesign.FieldBackground)
            .border(BorderStroke(1.dp, AuthDesign.OutlineVariant), AuthDesign.FieldShape)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = AuthDesign.OnSurfaceVariant.copy(alpha = 0.64f),
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = AuthDesign.OnSurfaceVariant.copy(alpha = 0.36f),
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            textStyle = LocalTextStyle.current.copy(
                color = AuthDesign.OnSurface,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = AuthDesign.Primary
            ),
            singleLine = true,
            shape = AuthDesign.FieldShape
        )
    }
}

@Composable
internal fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onTogglePasswordVisible: () -> Unit,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        modifier = modifier,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisible) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) {
                            R.drawable.ic_visibility
                        } else {
                            R.drawable.ic_visibility_off
                        }
                    ),
                    contentDescription = null,
                    tint = AuthDesign.OnSurfaceVariant.copy(alpha = 0.72f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    )
}

@Composable
internal fun AuthPrimaryButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(12.dp, AuthDesign.FieldShape),
        enabled = !isLoading,
        shape = AuthDesign.FieldShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthDesign.Primary,
            contentColor = Color.White,
            disabledContainerColor = AuthDesign.Primary.copy(alpha = 0.58f),
            disabledContentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
internal fun AuthErrorMessage(
    message: String?,
    modifier: Modifier = Modifier
) {
    if (!message.isNullOrBlank()) {
        Text(
            text = message,
            modifier = modifier.fillMaxWidth(),
            color = AuthDesign.Error,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
internal fun AuthSocialSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = AuthDesign.OnSurfaceVariant.copy(alpha = 0.22f)
            )
            Text(
                text = "HOẶC TIẾP TỤC VỚI",
                modifier = Modifier.padding(horizontal = 14.dp),
                color = AuthDesign.OnSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = AuthDesign.OnSurfaceVariant.copy(alpha = 0.22f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthSocialButton(text = "f", contentColor = Color(0xFF1877F2))
            AuthSocialButton(text = "A", contentColor = Color.Black)
            AuthSocialButton(text = "G", contentColor = Color(0xFFDB4437))
        }
    }
}

@Composable
private fun AuthSocialButton(
    text: String,
    contentColor: Color,
    shape: Shape = AuthDesign.PillShape
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(8.dp, shape)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.88f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.92f)), shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

internal enum class AuthTab {
    Login,
    Register
}
