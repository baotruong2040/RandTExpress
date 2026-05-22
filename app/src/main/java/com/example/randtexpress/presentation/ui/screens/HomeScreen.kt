package com.example.randtexpress.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.example.randtexpress.R
import com.example.randtexpress.data.preferences.SessionData
import com.example.randtexpress.presentation.ui.components.ExitConfirmDialog
import com.example.randtexpress.presentation.ui.components.ProductCard
import com.example.randtexpress.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

private val HomeHorizontalPadding = 16.dp

@Composable
fun HomeScreen(
    sessionState: SessionData,
    onLogout: () -> Unit,
    onExitApp: () -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    onCartClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBannerClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Lỗi: ${uiState.errorMessage}", color = Color.Red)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFAFAFA))
                            .padding(top = paddingValues.calculateTopPadding())
                    ) {
                        item { HomeHeader(onCartClick) }
                        item { SearchBar(onSearchClick) }
                        item { PromotionBanner(onBannerClick = onBannerClick) }
                        
                        uiState.categorySections.forEach { section ->
                            item {
                                SectionHeader(
                                    title = section.name,
                                    onSeeMoreClick = { onCategoryClick(section.name) }
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = HomeHorizontalPadding),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(section.products) { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = { onProductClick(product.id) },
                                            onAddToCart = { viewModel.addToCart(product) }
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Thêm khoảng trống ở cuối để không bị Bottom Navigation che mất nội dung
                        item { Spacer(modifier = Modifier.height(100.dp)) }
                    }
                }
            }

            // Thanh điều hướng nổi lên trên cùng, căn giữa dưới cùng
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent) // Đảm bảo phần bao quanh là trong suốt
            ) {
                HomeBottomNavigation(onLogout)
            }
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
private fun HomeHeader(onCartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeHorizontalPadding, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.location),
                tint = Color(0xFFDB0007),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.your_location),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }

        IconButton(onClick = onCartClick) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = stringResource(R.string.cart_title),
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun SearchBar(onSearchClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeHorizontalPadding, vertical = 6.dp)
            .clickable(onClick = onSearchClick),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFEEEEEE)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Tìm kiếm",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.search_hint),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

private data class HomeBannerUiModel(
    val title: String,
    val subtitle: String,
    val description: String,
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val accentColor: Color
)

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PromotionBanner(
    onBannerClick: (Int) -> Unit
) {
    val banners = remember {
        listOf(
            HomeBannerUiModel(
                title = "Khung giờ vàng",
                subtitle = "Ưu đãi nổi bật",
                description = "Banner này là placeholder để bạn thay ảnh sau.",
                backgroundStart = Color(0xFFDB0007),
                backgroundEnd = Color(0xFF8F0000),
                accentColor = Color(0xFFFFC107)
            ),
            HomeBannerUiModel(
                title = "Món mới",
                subtitle = "Khám phá ngay",
                description = "Chạm vào banner để gắn hành động sau này.",
                backgroundStart = Color(0xFF2E7D32),
                backgroundEnd = Color(0xFF1B5E20),
                accentColor = Color(0xFFA5D6A7)
            ),
            HomeBannerUiModel(
                title = "Deal cuối tuần",
                subtitle = "Chỉ dành riêng cho bạn",
                description = "Ảnh có thể thay đổi dễ dàng bằng dữ liệu thật.",
                backgroundStart = Color(0xFF1565C0),
                backgroundEnd = Color(0xFF0D47A1),
                accentColor = Color(0xFF90CAF9)
            )
        )
    }
    val startPage = remember(banners.size) {
        val midpoint = Int.MAX_VALUE / 2
        midpoint - (midpoint % banners.size)
    }
    val pagerState = rememberPagerState(initialPage = startPage)
    val currentPage by remember {
        derivedStateOf { pagerState.currentPage % banners.size }
    }

    LaunchedEffect(pagerState, banners.size) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeHorizontalPadding, vertical = 8.dp)
    ) {
        HorizontalPager(
            count = Int.MAX_VALUE,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) { page ->
            val banner = banners[page % banners.size]
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(page % banners.size) },
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 4.dp,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(banner.backgroundStart, banner.backgroundEnd)
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = banner.subtitle,
                                color = banner.accentColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = banner.title,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = banner.description,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (index == currentPage) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentPage) Color(0xFFDB0007) else Color(0xFFDB0007).copy(alpha = 0.35f)
                        )
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeHorizontalPadding, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Surface(
            onClick = onSeeMoreClick,
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFDB0007)
        ) {
            Text(
                text = stringResource(R.string.see_more),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun HomeBottomNavigation(onLogout: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp, // Đặt về 0 để tránh màu xám của Material 3
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(32.dp))
            .height(60.dp)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFDB0007),
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = "Đơn hàng") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Yêu thích") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Thông báo") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onLogout,
            icon = {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Tài khoản")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeBottomNavigation(onLogout = {});
}
