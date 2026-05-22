package com.example.randtexpress.data.remote

object ImageUrlResolver {
    fun resolve(rawUrl: String?): String? {
        if (rawUrl.isNullOrBlank()) return null
        val trimmed = rawUrl.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return replaceLocalhostIfNeeded(trimmed)
        }
        val base = NetworkConfig.IMAGE_BASE_URL.trimEnd('/')
        val path = if (trimmed.startsWith("/")) trimmed else "/$trimmed"
        return "$base$path"
    }

    private fun replaceLocalhostIfNeeded(url: String): String {
        val schemeIndex = url.indexOf("://")
        if (schemeIndex <= 0) return url
        val hostStart = schemeIndex + 3
        val pathStart = url.indexOf('/', hostStart)
        val hostPort = if (pathStart == -1) url.substring(hostStart) else url.substring(hostStart, pathStart)
        if (hostPort.startsWith("localhost") || hostPort.startsWith("127.0.0.1")) {
            val base = NetworkConfig.IMAGE_BASE_URL.trimEnd('/')
            val path = if (pathStart == -1) "" else url.substring(pathStart)
            return "$base$path"
        }
        return url
    }
}
