package com.example.campusfix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Colors Material doesn't have a slot for (status chips). */
@Immutable
data class ExtraColors(
    val amber: Color, val amberBg: Color,
    val blue: Color, val blueBg: Color,
    val resolved: Color, val resolvedBg: Color
)

val LightExtraColors = ExtraColors(
    amber = Color(0xFFB26A00), amberBg = Color(0xFFFFF1D6),
    blue = Color(0xFF2F5DA8), blueBg = Color(0xFFE6EEF9),
    resolved = Color(0xFF14513F), resolvedBg = Color(0xFFD3EADF)
)
val DarkExtraColors = ExtraColors(
    amber = Color(0xFFFFC670), amberBg = Color(0xFF3A2C12),
    blue = Color(0xFF9DBBF0), blueBg = Color(0xFF1C2A44),
    resolved = Color(0xFFA8E3CC), resolvedBg = Color(0xFF21463A)
)
val LocalExtraColors = staticCompositionLocalOf { LightExtraColors }

// These follow the active theme (light or dark) automatically.
val CfGreen: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary
val CfOnGreen: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onPrimary
val CfMint: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primaryContainer
val CfBackground: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.background
val CfSurface: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surface
val CfText: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onBackground
val CfMuted: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant
val CfBorder: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant
val CfAmber: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.amber
val CfAmberBg: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.amberBg
val CfBlue: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.blue
val CfBlueBg: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.blueBg
val CfResolved: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.resolved
val CfResolvedBg: Color @Composable @ReadOnlyComposable get() = LocalExtraColors.current.resolvedBg
