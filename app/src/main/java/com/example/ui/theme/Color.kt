package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// VIBRA Core Neon Brand Palette
val VibraViolet = Color(0xFF8B5CF6)
val VibraVioletDark = Color(0xFF6D28D9)
val VibraMagenta = Color(0xFFEC4899)
val VibraCyan = Color(0xFF06B6D4)
val VibraAmber = Color(0xFFF59E0B)

// Dark Theme Colors (Obsidian Cosmic)
val VibraDarkBg = Color(0xFF0A0713)
val VibraDarkSurface = Color(0xFF140E24)
val VibraDarkSurfaceCard = Color(0xFF1B1430)
val VibraDarkSurfaceHighlight = Color(0xFF261D42)
val VibraDarkBorder = Color(0x33A78BFA)
val VibraDarkTextPrimary = Color(0xFFF4F0FF)
val VibraDarkTextSecondary = Color(0xFF9E95B8)

// Light Theme Colors (Crisp Modern)
val VibraLightBg = Color(0xFFF8F6FD)
val VibraLightSurface = Color(0xFFFFFFFF)
val VibraLightSurfaceCard = Color(0xFFF2EDFC)
val VibraLightSurfaceHighlight = Color(0xFFE9E0FA)
val VibraLightBorder = Color(0x228B5CF6)
val VibraLightTextPrimary = Color(0xFF160E28)
val VibraLightTextSecondary = Color(0xFF6B6282)

// Functional & Accents
val HeartRed = Color(0xFFFF2A6D)
val OnlineGreen = Color(0xFF10B981)

// Gradient definitions
val VibraBrandGradient = Brush.horizontalGradient(
    listOf(VibraViolet, VibraMagenta, VibraCyan)
)

val VibraStoryRingGradient = Brush.sweepGradient(
    listOf(VibraMagenta, VibraViolet, VibraCyan, VibraMagenta)
)
