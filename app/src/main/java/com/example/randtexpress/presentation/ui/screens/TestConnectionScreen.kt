package com.example.randtexpress.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randtexpress.presentation.viewmodel.TestConnectionViewModel
import com.example.randtexpress.presentation.viewmodel.TestResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConnectionScreen(
    viewModel: TestConnectionViewModel = hiltViewModel()
) {
    val testResults = viewModel.testResults.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Kiểm tra kết nối Backend",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Kiểm tra các API",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TestButton(label = "Kiểm tra đăng nhập", onClick = { viewModel.testLogin() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Kiểm tra đăng ký", onClick = { viewModel.testRegister() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Lấy sản phẩm", onClick = { viewModel.testGetProducts() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Lấy danh mục", onClick = { viewModel.testGetCategories() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Tạo đơn hàng", onClick = { viewModel.testCreateOrder() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Lấy thông báo", onClick = { viewModel.testGetNotifications() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Đánh dấu đã đọc", onClick = { viewModel.testMarkNotificationRead() }, isLoading = isLoading)
                }
                item {
                    TestButton(label = "Lấy người dùng", onClick = { viewModel.testGetUsers() }, isLoading = isLoading)
                }
                item {
                    Button(
                        onClick = { viewModel.clearResults() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Xóa kết quả", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (testResults.isNotEmpty()) {
                Text(
                    "Kết quả (${testResults.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(testResults) { result ->
                        TestResultCard(result)
                    }
                }
            } else if (!isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Nhấn nút để kiểm tra endpoint",
                        color = Color(0xFF757575),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TestButton(
    label: String,
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Bold)
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Composable
fun TestResultCard(result: TestResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (result.isSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = result.testName,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                fontSize = 14.sp
            )
            Icon(
                imageVector = if (result.isSuccess) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (result.isSuccess) "Thành công" else "Lỗi",
                tint = if (result.isSuccess) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                modifier = Modifier.height(20.dp)
            )
        }

        Text(
            text = result.message,
            color = if (result.isSuccess) Color(0xFF2E7D32) else Color(0xFFC62828),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (result.data != null) {
            Text(
                text = result.data,
                color = Color(0xFF424242),
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEEEEEE))
                    .padding(8.dp)
            )
        }
    }
}
