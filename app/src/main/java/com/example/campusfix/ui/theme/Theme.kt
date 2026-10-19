package com.example.campusfix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1C6B5A), onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F1EC), onPrimaryContainer = Color(0xFF0F3F34),
    background = Color(0xFFF5F7F6), onBackground = Color(0xFF14201C),
    surface = Color.White, onSurface = Color(0xFF14201C),
    surfaceVariant = Color(0xFFEEF2F0), onSurfaceVariant = Color(0xFF6B7A75),
    outline = Color(0xFFC5CFCB), outlineVariant = Color(0xFFE2E8E5)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4FBFA5), onPrimary = Color(0xFF00241C),
    primaryContainer = Color(0xFF1F3D35), onPrimaryContainer = Color(0xFFBFEBDD),
    background = Color(0xFF0F1513), onBackground = Color(0xFFE4EBE8),
    surface = Color(0xFF1A211F), onSurface = Color(0xFFE4EBE8),
    surfaceVariant = Color(0xFF232C29), onSurfaceVariant = Color(0xFF9DAFA8),
    outline = Color(0xFF4A5853), outlineVariant = Color(0xFF2A3532)
)

/** Follows the phone's light/dark setting automatically. */
@Composable
fun CampusFixTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalExtraColors provides if (darkTheme) DarkExtraColors else LightExtraColors) {
        MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
    }
}
