package de.pdfwerkzeugkasten.ui.tools

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.pdfwerkzeugkasten.domain.model.*
import de.pdfwerkzeugkasten.ui.MainViewModel
import de.pdfwerkzeugkasten.ui.ToolUiState
import de.pdfwerkzeugkasten.ui.home.iconFor
import de.pdfwerkzeugkasten.util.humanSize
import java.io.File

@Composable fun ToolFlowScreen(type: PdfToolType, vm: MainViewModel, onBack: () -> Unit) {
    val state by vm.toolState.collectAsStateWithLifecycle(); val context = LocalContext.current
    val pickPdf = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { vm.setInputs(it) }
    val pickImages = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(50)) { vm.setInputs(it) }
    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) { Icon(iconFor(type), null, tint=MaterialTheme.colorScheme.primary); Text(title(type), style=MaterialTheme.typography.headlineSmall, fontWeight=FontWeight.Bold) } }
        item { StepCard("1", "Datei auswählen") { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Button(onClick={ if(type == PdfToolType.IMAGES_TO_PDF) pickImages.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) else pickPdf.launch(arrayOf("application/pdf")) }) { Text(if(type == PdfToolType.IMAGES_TO_PDF) "Bilder auswählen" else "PDF auswählen") }; OutlinedButton(onClick=onBack){Text("Zurück")} } } }
        val ready = state as? ToolUiState.Ready
        if (ready != null) {
            items(ready.inputs) { Text("• ${it.displayName} (${it.sizeBytes.humanSize()})") }
            item { StepCard("2", "Optionen") { Options(type, ready, vm) } }
            item { Button(onClick={vm.run(type)}, enabled=ready.inputs.isNotEmpty(), modifier=Modifier.fillMaxWidth()) { Text(action(type)) } }
        }
        when(val s = state) {
            is ToolUiState.Processing -> item { ProcessingCard(s.text) }
            is ToolUiState.Done -> item { ResultCard(s.result, type, share = { shareUri(context, s.result.uri, s.result.fileName) }) }
            is ToolUiState.Error -> item { Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)) { Text(s.message, Modifier.padding(16.dp)) } }
            else -> {}
        }
    }
}
@Composable private fun StepCard(num: String, title: String, content: @Composable ColumnScope.() -> Unit) { ElevatedCard { Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) { Text("Schritt $num · $title", fontWeight=FontWeight.SemiBold); content() } } }
@Composable private fun Options(type: PdfToolType, ready: ToolUiState.Ready, vm: MainViewModel) { when(type){ PdfToolType.COMPRESS -> Row { CompressionLevel.entries.forEach { FilterChip(selected=ready.level==it, onClick={vm.updateReady{r->r.copy(level=it)}}, label={Text(it.title)}, modifier=Modifier.padding(end=6.dp)) } }; PdfToolType.SPLIT -> OutlinedTextField(ready.range, {v->vm.updateReady{it.copy(range=v)}}, label={Text("Seitenbereich, z. B. 1-3, 5")}); PdfToolType.ROTATE -> Row { listOf(90,180,270).forEach { FilterChip(ready.rotation==it, {vm.updateReady{r->r.copy(rotation=it)}}, label={Text("$it°")}, modifier=Modifier.padding(end=6.dp)) } }; PdfToolType.PROTECT, PdfToolType.UNLOCK -> OutlinedTextField(ready.password, {v->vm.updateReady{it.copy(password=v)}}, label={Text("Passwort")}); else -> Text("Prüfe Reihenfolge und starte den Vorgang.") } }
@Composable private fun ProcessingCard(text: String) { ElevatedCard { Column(Modifier.padding(16.dp)) { Text(text); LinearProgressIndicator(Modifier.fillMaxWidth().padding(top=12.dp)); Text("Keine Werbung während der Verarbeitung.") } } }
@Composable private fun ResultCard(result: PdfResult, type: PdfToolType, share: () -> Unit) { ElevatedCard { Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) { Text("Fertig", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Bold); Text(result.fileName); Text("Vorher: ${result.inputSizeBytes.humanSize()} · Nachher: ${result.outputSizeBytes.humanSize()}"); if(type==PdfToolType.COMPRESS && result.inputSizeBytes>0) Text("Ersparnis: ${((1 - result.outputSizeBytes.toDouble()/result.inputSizeBytes) * 100).toInt().coerceAtLeast(0)} %"); Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Button(onClick=share){Text("Teilen")}; OutlinedButton(onClick=share){Text("Öffnen/Speichern")}; OutlinedButton(onClick={}){Text("Neuer Vorgang")} } } } }
private fun shareUri(context: android.content.Context, uri: Uri, name: String) { val out = if(uri.scheme == "file") FileProvider.getUriForFile(context, context.packageName + ".files", File(uri.path!!)) else uri; context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("application/pdf").putExtra(Intent.EXTRA_STREAM, out).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), name)) }
private fun title(type: PdfToolType) = when(type){ PdfToolType.COMPRESS->"PDF komprimieren"; PdfToolType.MERGE->"PDFs zusammenführen"; PdfToolType.SPLIT->"PDF teilen"; PdfToolType.IMAGES_TO_PDF->"Bilder zu PDF"; PdfToolType.ROTATE->"Seiten drehen"; PdfToolType.PROTECT->"PDF schützen"; PdfToolType.UNLOCK->"PDF entsperren"; else->"Werkzeug" }
private fun action(type: PdfToolType) = when(type){ PdfToolType.COMPRESS->"PDF komprimieren"; PdfToolType.MERGE->"PDF zusammenführen"; PdfToolType.SPLIT->"Seiten extrahieren"; PdfToolType.IMAGES_TO_PDF->"PDF generieren"; PdfToolType.ROTATE->"Neue PDF speichern"; PdfToolType.PROTECT->"PDF schützen"; PdfToolType.UNLOCK->"PDF entsperren"; else->"Starten" }
