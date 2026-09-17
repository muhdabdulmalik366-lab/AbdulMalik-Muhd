package com.example.ui.theme

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
    primary = VibraViolet,
    onPrimary = Color.White,
    primaryContainer = VibraDarkSurfaceHighlight,
    onPrimaryContainer = VibraDarkTextPrimary,
    secondary = VibraMagenta,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF38152B),
    onSecondaryContainer = Color(0xFFFFD8E9),
    tertiary = VibraCyan,
    onTertiary = Color.Black,
    background = VibraDarkBg,
    onBackground = VibraDarkTextPrimary,
    surface = VibraDarkSurface,
    onSurface = VibraDarkTextPrimary,
    surfaceVariant = VibraDarkSurfaceCard,
    onSurfaceVariant = VibraDarkTextSecondary,
    outline = VibraDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = VibraVioletDark,
    onPrimary = Color.White,
    primaryContainer = VibraLightSurfaceHighlight,
    onPrimaryContainer = VibraLightTextPrimary,
    secondary = VibraMagenta,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFD8E9),
    onSecondaryContainer = Color(0xFF3A0D23),
    tertiary = Color(0xFF0891B2),
    onTertiary = Color.White,
    background = VibraLightBg,
    onBackground = VibraLightTextPrimary,
    surface = VibraLightSurface,
    onSurface = VibraLightTextPrimary,
    surfaceVariant = VibraLightSurfaceCard,
    onSurfaceVariant = VibraLightTextSecondary,
    outline = VibraLightBorder
)

@Composable
fun VibraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded colors for distinctive identity
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
