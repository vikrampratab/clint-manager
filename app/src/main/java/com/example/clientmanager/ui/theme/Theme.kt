package com.example.clientmanager.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = WellnessGreen,
    onPrimary = Color.WhiteCompat,
    primaryContainer = WellnessGreenLight,
    onPrimaryContainer = WellnessGreenDark,
    secondary = WellnessGold,
    background = Color.WhiteCompat,
    surface = Color.WhiteCompat,
    error = StatusPending
)

private val AppTypography = Typography(
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelSmall = TextStyle(fontSize = 11.sp)
)

@Composable
fun ClientManagerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}

// small helper so Color.WhiteCompat reads cleanly above
private object Color {
    val WhiteCompat = androidx.compose.ui.graphics.Color.White
}
