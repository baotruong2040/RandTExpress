package com.example.randtexpress.presentation.ui

import java.text.Normalizer
import java.util.Locale

object FixedCategories {
    const val COMBO = "Combo"
    const val FRIED_CHICKEN = "Gà rán"
    const val BURGER = "Burger"
    const val DRINK = "Đồ uống"

    val orderedCategories = listOf(COMBO, FRIED_CHICKEN, BURGER, DRINK)

    fun mapRawCategoryToFixed(rawCategoryName: String): String? {
        val normalized = normalize(rawCategoryName)

        return when {
            normalized.contains("combo") -> COMBO
            (normalized.contains("ga") && normalized.contains("ran")) ||
                normalized.contains("friedchicken") ||
                normalized.contains("chicken") -> FRIED_CHICKEN
            normalized.contains("burger") -> BURGER
            normalized.contains("douong") ||
                normalized.contains("drink") ||
                normalized.contains("beverage") ||
                normalized.contains("nuoc") -> DRINK
            else -> null
        }
    }

    private fun normalize(value: String): String {
        val deAccent = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return deAccent.lowercase(Locale.ROOT).replace("\\s+".toRegex(), "")
    }
}
