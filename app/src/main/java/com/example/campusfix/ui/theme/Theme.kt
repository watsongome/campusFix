package com.example.campusfix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CampusFixColorScheme = lightColorScheme(

    primary = CampusGreen,
    onPrimary = CampusSurface,

    primaryContainer = CampusGreenLight,
    onPrimaryContainer = CampusGreen,

    secondary = CampusGreen,
    onSecondary = CampusSurface,

    secondaryContainer = CampusGreenLight,
    onSecondaryContainer = CampusGreen,

    background = CampusBackground,
    onBackground = CampusTextPrimary,

    surface = CampusSurface,
    onSurface = CampusTextPrimary,

    surfaceVariant = CampusGreenLight,
    onSurfaceVariant = CampusTextSecondary,

    outline = CampusBorder,

    error = CampusError,
    onError = CampusSurface,

    errorContainer = CampusErrorBackground,
    onErrorContainer = CampusError
)

@Composable
fun CampusFixTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CampusFixColorScheme,
        typography = Typography,
        content = content
    )
}