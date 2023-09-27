package com.groupec.githubfetchercompose.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = Green,
    onPrimary = White,
    primaryContainer = LightGreen,
    onPrimaryContainer = Black,
    secondary = Yellow,
    onSecondary = White,
    secondaryContainer = LightYellow,
    onSecondaryContainer = Black,
    tertiary = Green,
    onTertiary = White,
    tertiaryContainer = LightGreen,
    onTertiaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = Green,
    onSurface = White,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    outline = Gray,
    outlineVariant = LightGray,
    error = Red,
    onError = White,
    errorContainer = LightRed,
    onErrorContainer = DarkRed
)

private val LightColorPalette = lightColorScheme(
    primary = Green,
    onPrimary = White,
    primaryContainer = LightGreen,
    onPrimaryContainer = Black,
    secondary = Yellow,
    onSecondary = White,
    secondaryContainer = LightYellow,
    onSecondaryContainer = Black,
    tertiary = Green,
    onTertiary = White,
    tertiaryContainer = LightGreen,
    onTertiaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = Green,
    onSurface = White,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    outline = Gray,
    outlineVariant = LightGray,
    error = Red,
    onError = White,
    errorContainer = LightRed,
    onErrorContainer = DarkRed
)

@Composable
fun MainTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}