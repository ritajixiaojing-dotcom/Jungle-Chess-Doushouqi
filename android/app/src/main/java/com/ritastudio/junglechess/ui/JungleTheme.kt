package com.ritastudio.junglechess.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BoardWoodBackground = Color(0xFFF3EFE0)
val BoardBorderColor = Color(0xFF795548)
val RiverColor = Color(0xFF81D4FA)
val RiverDeepColor = Color(0xFF29B6F6)
val LandColor = Color(0xFFF9FBE7)
val TrapColorRed = Color(0xFFFFEBEE)
val TrapColorBlue = Color(0xFFE3F2FD)
val DenColorRed = Color(0xFFFFCDD2)
val DenColorBlue = Color(0xFFBBDEFB)

val BluePlayerColor = Color(0xFF1976D2)
val RedPlayerColor = Color(0xFFD32F2F)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    secondary = Color(0xFF1565C0),
    background = Color(0xFFF1F8E9),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun JungleChessTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
