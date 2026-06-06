package de.pdfwerkzeugkasten.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(primary = Color(0xFFD7263D), secondary = Color(0xFF1464F4), background = Color(0xFFF7F9FC), surface = Color.White)
private val Dark = darkColorScheme(primary = Color(0xFFFF6B7A), secondary = Color(0xFF8AB4FF), background = Color(0xFF101216), surface = Color(0xFF181B21))
@Composable fun PdfWerkzeugkastenTheme(theme: String = "System", content: @Composable () -> Unit) { val dark = when(theme){"Hell"->false;"Dunkel"->true;else-> isSystemInDarkTheme()}; MaterialTheme(colorScheme = if(dark) Dark else Light, typography = Typography(), content = content) }
