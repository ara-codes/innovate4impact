package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MedicaTealLight,
    onPrimary = MedicaNavyDark,
    primaryContainer = MedicaNavyLight,
    onPrimaryContainer = MedicaNavyContainer,
    secondary = MedicaTealLight,
    onSecondary = MedicaNavyDark,
    secondaryContainer = MedicaTealDark,
    onSecondaryContainer = MedicaTealContainer,
    tertiary = MedicaAmber,
    background = MedicaDarkBackground,
    surface = MedicaDarkSurface,
    surfaceVariant = MedicaDarkSurfaceVariant,
    onBackground = MedicaDarkTextPrimary,
    onSurface = MedicaDarkTextPrimary,
    onSurfaceVariant = MedicaDarkTextSecondary,
    outline = MedicaDarkOutline,
    outlineVariant = MedicaDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = MedicaNavyPrimary,
    onPrimary = MedicaSurface,
    primaryContainer = MedicaNavyContainer,
    onPrimaryContainer = MedicaNavyOnContainer,
    secondary = MedicaTealPrimary,
    onSecondary = MedicaSurface,
    secondaryContainer = MedicaTealContainer,
    onSecondaryContainer = MedicaTealOnContainer,
    tertiary = MedicaAmber,
    background = MedicaBackground,
    surface = MedicaSurface,
    surfaceVariant = MedicaSurfaceVariant,
    onBackground = MedicaTextPrimary,
    onSurface = MedicaTextPrimary,
    onSurfaceVariant = MedicaTextSecondary,
    outline = MedicaOutline,
    outlineVariant = MedicaOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand colors for cohesive healthcare identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
