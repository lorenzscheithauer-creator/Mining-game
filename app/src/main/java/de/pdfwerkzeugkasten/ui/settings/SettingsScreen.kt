package de.pdfwerkzeugkasten.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.pdfwerkzeugkasten.domain.model.CompressionLevel
import de.pdfwerkzeugkasten.ui.MainViewModel

@Composable fun SettingsScreen(vm: MainViewModel, onPrivacy: () -> Unit) { val settings by vm.settings.collectAsStateWithLifecycle(); Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) { Text("Einstellungen", style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Bold); Text("Design"); Row { listOf("System","Hell","Dunkel").forEach { FilterChip(settings.theme==it, {vm.setTheme(it)}, label={Text(it)}, modifier=Modifier.padding(end=6.dp)) } }; Text("Standard-Komprimierung"); Row { CompressionLevel.entries.forEach { FilterChip(settings.defaultCompression==it, {vm.setCompression(it)}, label={Text(it.title)}, modifier=Modifier.padding(end=6.dp)) } }; SettingsButton("Datenschutzrichtlinie", onPrivacy); SettingsButton("Werbung & Zustimmung verwalten") {}; SettingsButton("Pro wiederherstellen") {}; SettingsButton("Feedback senden") {}; SettingsButton("App bewerten") {}; SettingsButton("Impressum") {}; SettingsButton("Open-Source-Lizenzen") {}; Text("Open Source: PDFBox-Android (Apache-2.0), AndroidX/Compose/Room/WorkManager/DataStore (Apache-2.0), Coil (Apache-2.0). Google SDKs: Play Services/AdMob/UMP/Billing gemäß Google Terms.") } }
@Composable private fun SettingsButton(text: String, onClick: () -> Unit) { OutlinedButton(onClick=onClick, Modifier.fillMaxWidth()) { Text(text) } }

@Composable fun PrivacyPolicyScreen() { Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) { Text("Datenschutzrichtlinie", style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Bold); Text(privacyText) } }
private val privacyText = """
PDF Werkzeugkasten verarbeitet ausgewählte PDF- und Bilddateien lokal auf deinem Gerät. Wir betreiben im MVP keine eigenen Server für Dokumentverarbeitung und laden keine PDF-Inhalte ohne deine ausdrückliche Aktion hoch.

Gespeicherte Daten: Einstellungen, Pro-Status und Verlauf-Metadaten wie Werkzeugtyp, Dateiname, Datum, Eingabe-/Ausgabegröße und optional eine Ergebnis-URI. Originaldokumente werden nicht ungefragt kopiert.

Werbung: Google AdMob kann Gerätekennungen einschließlich Advertising ID, IP-Adresse und Nutzungsdaten verarbeiten. Für EU-Nutzer wird ein Consent-Flow über Google UMP angeboten. Ohne Zustimmung werden, soweit verfügbar, nicht-personalisierte Anzeigen genutzt.

In-App-Käufe: Google Play Billing verarbeitet Kaufstatus und Transaktionsdaten. Die App speichert den letzten bekannten Pro-Status lokal, damit Pro-Funktionen offline erhalten bleiben.

Crashlogs: Firebase Crashlytics ist im MVP nicht aktiviert. Falls später aktiviert, werden keine PDF-Inhalte in Crashlogs protokolliert.

Datenlöschung: Verlauf kann in der App gelöscht werden. App-Daten können über Android-Systemeinstellungen entfernt werden. Kontakt: support@example.com.
""".trimIndent()
