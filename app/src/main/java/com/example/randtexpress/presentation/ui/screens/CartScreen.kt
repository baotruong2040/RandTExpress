package com.example.randtexpress.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.randtexpress.R
import com.example.randtexpress.domain.model.CartItem
import com.example.randtexpress.presentation.ui.toVndDisplay
import com.example.randtexpress.presentation.viewmodel.CartViewModel

// Design System Colors from code.html & DESIGN.md
private val ColorPrimary = Color(0xFFbd0d00)
private val ColorSecondaryContainer = Color(0xFFfe9832)
private val ColorSecondaryFixedDim = Color(0xFFffb77a)
private val ColorSurface = Color(0xFFfcf9f8)
private val ColorSurfaceBright = Color(0xFFfcf9f8)
private val ColorSurfaceContainerLow = Color(0xFFf6f3f2)
private val ColorSurfaceVariant = Color(0xFFe5e2e1)
private val ColorOnSurface = Color(0xFF1c1b1b)
private val ColorOnSurfaceVariant = Color(0xFF5d403a)
private val ColorOutline = Color(0xFF916f69)
private val ColorOnPrimary = Color(0xFFffffff)
private val ColorOnSecondaryContainer = Color(0xFF683700)
private val ColorTertiary = Color(0xFF006b4a)

@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onOrderNowClick: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurface)
    ) {
        // Gradient Header Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            ColorPrimary,
                            ColorSecondaryContainer,
                            ColorSecondaryFixedDim
                        )
                    )
                )
        )

        // Top App Bar Elements (Custom)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing) // Tương đương h-safe top
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = ColorOnPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = stringResource(R.string.cart_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorOnPrimary,
                letterSpacing = 1.sp
            )
            
            if (uiState.isEmpty) {
                Spacer(modifier = Modifier.size(48.dp)) // Placeholder to keep title centered
            } else {
                IconButton(onClick = { viewModel.clearCart() }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Clear cart",
                        tint = ColorOnPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Main Content Area (White overlapping card)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp) // Offset để chồng lên gradient
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(ColorSurface)
        ) {
            // Drag Handle Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(6.dp)
                        .background(ColorSurfaceVariant, CircleShape)
                )
            }

            if (uiState.isEmpty) {
                EmptyCartContent(
                    onOrderNowClick = onBackClick, // Nếu giỏ trống, click để quay lại mua sắm
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 180.dp) // Space for bottom bar
                ) {
                    items(uiState.items, key = { it.productId }) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { viewModel.increaseQuantity(item) },
                            onDecrease = { viewModel.decreaseQuantity(item) }
                        )
                    }
                }
            }
        }

        // Fixed Bottom Checkout Area
        if (!uiState.isEmpty) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(ColorSurface)
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                // Promo & Payment Method row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Payment Selector
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { /* TBD */ }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = "Payment Method",
                            tint = ColorTertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Cash",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorOnSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "Select Payment",
                            tint = ColorOnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(ColorSurfaceVariant)
                    )

                    // Promo Action
                    Text(
                        text = "ADD A PROMO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorOnSurfaceVariant,
                        modifier = Modifier
                            .clickable { /* TBD */ }
                            .padding(end = 8.dp)
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorSurfaceVariant.copy(alpha = 0.5f))
                )

                // Total & Order Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorSurfaceBright)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorOnSurface
                        )
                        Text(
                            text = uiState.totalPrice.toVndDisplay(),
                            fontSize = 16.sp,
                            color = ColorOutline,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Button(
                        onClick = onOrderNowClick,
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrimary
                        )
                    ) {
                        Text(
                            text = "Order",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorOnPrimary,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Image
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ColorSecondaryFixedDim.copy(alpha = 0.5f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = ColorOnSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Decrease
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorSecondaryContainer.copy(alpha = 0.3f))
                            .clickable(onClick = onDecrease),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = ColorOnSecondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorOnSurface
                    )

                    // Increase
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorSecondaryContainer)
                            .clickable(onClick = onIncrease),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = ColorOnPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Price
                Text(
                    text = item.price.toVndDisplay(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ColorPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notes Input (Mock)
            var noteText by remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorSurfaceContainerLow)
                    .border(
                        width = 1.dp,
                        color = ColorSurfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = ColorOnSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (noteText.isEmpty()) {
                            Text(
                                text = "Add notes here",
                                fontSize = 14.sp,
                                color = ColorOutline
                            )
                        }
                        innerTextField()
                    }
                )
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit notes",
                    tint = ColorOutline,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyCartContent(
    onOrderNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_cart),
            color = ColorOutline,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(ColorSecondaryContainer.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                tint = ColorPrimary,
                modifier = Modifier.size(96.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onOrderNowClick,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorPrimary
            )
        ) {
            Text("Back to Menu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}