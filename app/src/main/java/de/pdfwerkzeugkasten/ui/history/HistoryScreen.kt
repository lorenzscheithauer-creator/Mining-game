package de.pdfwerkzeugkasten.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.pdfwerkzeugkasten.ui.MainViewModel
import de.pdfwerkzeugkasten.util.humanSize
import java.text.DateFormat
import java.util.Date

@Composable fun HistoryScreen(vm: MainViewModel) { val items by vm.history.collectAsStateWithLifecycle(); LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) { item { Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) { Text("Verlauf", style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Bold); TextButton(onClick=vm::clearHistory){Text("Löschen")} } }; item { Text("Der Verlauf speichert keine PDF-Inhalte, sondern nur Metadaten und Ergebnis-URIs.") }; items(items) { ElevatedCard { Column(Modifier.padding(16.dp)) { Text(it.displayName, fontWeight=FontWeight.SemiBold); Text("${DateFormat.getDateTimeInstance().format(Date(it.createdAt))} · ${it.outputSizeBytes.humanSize()}") } } } } }
