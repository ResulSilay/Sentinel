package com.rs.sentinel.ui.ext

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.rs.sentinel.type.RiskLevel

@SuppressLint("SuspiciousModifierThen")
fun Modifier.sentinelGradientBackground(
    riskLevel: RiskLevel? = null,
    isReversed: Boolean = false,
): Modifier {
    val baseColor = when (riskLevel) {
        RiskLevel.SAFE -> Color(0xFF1B5E20)
        RiskLevel.LOW -> Color(0xFFFBC02D)
        RiskLevel.MEDIUM -> Color(0xFFF57C00)
        RiskLevel.HIGH -> Color(0xFFD32F2F)
        null -> Color(0xFF7E7E7E)
    }

    return then(
        background(
            brush = Brush.verticalGradient(
                colors = if (isReversed) {
                    listOf(
                        Color(0xFF000000),
                        Color(0xFF000000),
                        baseColor.copy(alpha = 0.12f)
                    )
                } else {
                    listOf(
                        baseColor.copy(alpha = 0.2f),
                        baseColor.copy(alpha = 0.01f),
                        Color(0xFF000000)
                    )
                }
            )
        )
    )
}