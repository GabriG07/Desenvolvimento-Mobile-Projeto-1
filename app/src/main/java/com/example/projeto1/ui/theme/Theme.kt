package com.example.projeto1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val AppDarkColors = darkColorScheme(
    primary = ColorGold,
    onPrimary = ColorBackground,
    secondary = ColorGold,
    onSecondary = ColorBackground,
    background = ColorBackground,
    onBackground = ColorTextPrimary,
    surface = ColorSurface,
    onSurface = ColorTextPrimary,
    surfaceVariant = ColorSurfaceDim,
    onSurfaceVariant = ColorTextSecondary,
    error = ColorError,
    onError = ColorTextPrimary
)

@Composable
fun Projeto1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppDarkColors,
        typography = AppTypography,
        content = content
    )
}