package com.rs.sentinel.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF1744),
    onPrimary = Color.White,
    secondary = Color(0xFFD32F2F),
    onSecondary = Color.White,
    tertiary = Color(0xFFB71C1C),
    background = Color(0xFF0A0101),
    onBackground = Color(0xFFFDEAEA),
    surface = Color(0xFF1A0505),
    onSurfaceVariant = Color(0xFFFFFFFF),
    error = Color(0xFFFF5252),
    outline = Color(0xFF442C2C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF1744),
    onPrimary = Color.White,
    secondary = Color(0xFFD32F2F),
    onSecondary = Color.White,
    tertiary = Color(0xFFB71C1C),
    background = Color(0xFF0A0101),
    onBackground = Color(0xFFFDEAEA),
    surface = Color(0xFF1A0505),
    onSurfaceVariant = Color(0xFFFFFFFF),
    error = Color(0xFFFF5252),
    outline = Color(0xFF442C2C)
)

@Composable
fun SentinelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}