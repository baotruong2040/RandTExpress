package com.example.randtexpress.presentation.ui

import java.text.NumberFormat
import java.util.Locale

private val vietnamLocale = Locale.forLanguageTag("vi-VN")

fun Long.toVndDisplay(): String {
    val formatted = NumberFormat.getNumberInstance(vietnamLocale).format(this)
    return "$formatted ₫"
}
