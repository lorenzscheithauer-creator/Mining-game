package de.pdfwerkzeugkasten.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable fun OnboardingScreen(onDone: () -> Unit) { var page by remember { mutableIntStateOf(0) }; val pages = listOf(Triple("Deine PDF-Werkzeuge an einem Ort", "Komprimiere, kombiniere und konvertiere PDFs direkt auf deinem Gerät.", Icons.Default.PictureAsPdf), Triple("Privat & lokal", "Deine Dateien werden lokal verarbeitet und nicht auf Server hochgeladen.", Icons.Default.Lock), Triple("Schnell teilen", "Speichere fertige PDFs oder teile sie direkt per Mail, WhatsApp und Cloud.", Icons.Default.Share)); val p=pages[page]
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) { Icon(p.third, null, Modifier.size(96.dp), tint=MaterialTheme.colorScheme.primary); Spacer(Modifier.height(24.dp)); Text(p.first, style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Bold); Spacer(Modifier.height(10.dp)); Text(p.second); Spacer(Modifier.height(34.dp)); Button(onClick={ if(page == pages.lastIndex) onDone() else page++ }) { Text(if(page == pages.lastIndex) "Loslegen" else "Weiter") } }
}
