package de.pdfwerkzeugkasten.ui.pro

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable fun ProScreen() { Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement=Arrangement.spacedBy(16.dp)) { Text("PDF Werkzeugkasten Pro", style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Bold); listOf("Keine Werbung", "Keine Limits", "Batch-Verarbeitung", "Schnellere Workflows", "Premium-Design").forEach { Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) { Icon(Icons.Default.CheckCircle, null, tint=MaterialTheme.colorScheme.primary); Text(it) } }; Button(onClick={}, modifier=Modifier.fillMaxWidth()) { Text("Lifetime Pro freischalten · Launch-Angebot 2,99 €") }; OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth()) { Text("Käufe wiederherstellen") }; Text("Alle digitalen Premium-Funktionen laufen über Google Play Billing. Offline wird der letzte bekannte Pro-Status respektiert.") } }
