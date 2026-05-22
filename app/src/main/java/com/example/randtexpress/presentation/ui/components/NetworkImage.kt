package com.example.randtexpress.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun NetworkImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    enableLogging: Boolean = true
) {
    if (imageUrl.isNullOrBlank()) {
        ImagePlaceholder(modifier)
        return
    }

    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .addHeader(
            "User-Agent",
            "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        )
        .crossfade(true)
        .listener(
            onError = { _, result ->
                if (enableLogging) {
                    Log.e("NetworkImage", "Failed to load image: $imageUrl", result.throwable)
                }
            }
        )
        .build()

    SubcomposeAsyncImage(
        model = request,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = { ImagePlaceholder(Modifier.fillMaxSize()) },
        error = { ImagePlaceholder(Modifier.fillMaxSize()) }
    )
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Image,
            contentDescription = null,
            tint = Color(0xFF8A8A8A),
            modifier = Modifier.size(28.dp)
        )
    }
}
