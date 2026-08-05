package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IndiaSaffron,
    onPrimary = SurfaceWhite,
    primaryContainer = DeepBlue,
    onPrimaryContainer = SurfaceWhite,
    secondary = BrightOrangeDark,
    onSecondary = SurfaceWhite,
    secondaryContainer = BrightOrange,
    onSecondaryContainer = SurfaceWhite,
    tertiary = IndiaGreen,
    onTertiary = SurfaceWhite,
    background = NavyDark,
    surface = Color(0xFF1E293B),
    onBackground = SoftGray,
    onSurface = SoftGray
)

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,
    onPrimary = SurfaceWhite,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = DeepBlue,
    secondary = BrightOrange,
    onSecondary = SurfaceWhite,
    secondaryContainer = OrangeLight,
    onSecondaryContainer = DeepBlue,
    tertiary = IndiaGreen,
    onTertiary = SurfaceWhite,
    background = SoftGray,
    surface = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun AdBazaarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
