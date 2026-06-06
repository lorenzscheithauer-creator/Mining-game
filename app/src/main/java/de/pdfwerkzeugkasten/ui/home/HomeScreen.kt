package de.pdfwerkzeugkasten.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.pdfwerkzeugkasten.domain.model.*

private val tools = listOf(
    PdfTool(PdfToolType.COMPRESS, "PDF komprimieren", "Dateigröße lokal reduzieren", "Compress"), PdfTool(PdfToolType.MERGE, "PDFs zusammenführen", "Mehrere Dateien kombinieren", "Merge"), PdfTool(PdfToolType.IMAGES_TO_PDF, "Bilder zu PDF", "Fotos sortieren und exportieren", "Image"), PdfTool(PdfToolType.SPLIT, "PDF teilen", "Seitenbereiche extrahieren", "Split"), PdfTool(PdfToolType.PDF_TO_IMAGES, "PDF zu Bildern", "Seiten als JPG/PNG", "Export"), PdfTool(PdfToolType.ROTATE, "Seiten drehen", "90/180/270 Grad", "Rotate"), PdfTool(PdfToolType.PROTECT, "Passwort schützen", "PDF verschlüsseln", "Lock"), PdfTool(PdfToolType.UNLOCK, "Passwort entfernen", "Nur mit bekanntem Passwort", "Unlock"), PdfTool(PdfToolType.PREVIEW, "PDF-Vorschau", "Seiten prüfen", "Preview"), PdfTool(PdfToolType.HISTORY, "Verlauf", "Letzte Vorgänge", "History")
)
@Composable fun HomeScreen(onTool: (PdfToolType) -> Unit) { var query by remember { mutableStateOf("") }; val filtered = tools.filter { it.title.contains(query, true) || it.description.contains(query, true) }
    LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        item(span={GridItemSpan(maxLineSpan)}) { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("PDF Werkzeugkasten", style=MaterialTheme.typography.headlineLarge, fontWeight=FontWeight.Bold); Text("Deine PDFs bleiben auf deinem Gerät. Wir laden keine Dokumente auf Server hoch."); OutlinedTextField(query, {query=it}, Modifier.fillMaxWidth(), placeholder={Text("PDF-Tool suchen")}, leadingIcon={Icon(Icons.Default.Search,null)}) } }
        items(filtered) { tool -> ElevatedCard(onClick={onTool(tool.id)}, modifier=Modifier.height(128.dp)) { Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.SpaceBetween) { Icon(iconFor(tool.id), null, tint=MaterialTheme.colorScheme.primary); Column { Text(tool.title, fontWeight=FontWeight.SemiBold); Text(tool.description, style=MaterialTheme.typography.bodySmall) } } } }
    }
}
fun iconFor(type: PdfToolType) = when(type){ PdfToolType.COMPRESS->Icons.Default.Compress; PdfToolType.MERGE->Icons.Default.CallMerge; PdfToolType.SPLIT->Icons.Default.ContentCut; PdfToolType.IMAGES_TO_PDF->Icons.Default.Image; PdfToolType.PDF_TO_IMAGES->Icons.Default.Collections; PdfToolType.ROTATE->Icons.Default.RotateRight; PdfToolType.PROTECT->Icons.Default.Lock; PdfToolType.UNLOCK->Icons.Default.LockOpen; PdfToolType.HISTORY->Icons.Default.History; else->Icons.Default.PictureAsPdf }
