package de.pdfwerkzeugkasten.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.pdfwerkzeugkasten.data.history.HistoryRepository
import de.pdfwerkzeugkasten.data.pdf.PdfEngine
import de.pdfwerkzeugkasten.data.settings.AppSettings
import de.pdfwerkzeugkasten.data.settings.SettingsRepository
import de.pdfwerkzeugkasten.domain.model.*
import de.pdfwerkzeugkasten.domain.usecase.PlanLimits
import de.pdfwerkzeugkasten.util.displayName
import de.pdfwerkzeugkasten.util.sizeBytes
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ToolUiState { data object Idle: ToolUiState(); data class Ready(val inputs: List<SelectedFile> = emptyList(), val level: CompressionLevel = CompressionLevel.MEDIUM, val range: String = "1", val rotation: Int = 90, val password: String = "", val allowPrint: Boolean = true, val allowCopy: Boolean = false, val message: String? = null): ToolUiState(); data class Processing(val text: String): ToolUiState(); data class Done(val result: PdfResult): ToolUiState(); data class Error(val message: String): ToolUiState() }

class MainViewModel(private val app: android.app.Application, private val settingsRepo: SettingsRepository, private val historyRepo: HistoryRepository, private val engine: PdfEngine) : ViewModel() {
    private val limits = PlanLimits()
    val settings: StateFlow<AppSettings> = settingsRepo.settings.stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())
    val history = historyRepo.observe().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _toolState = MutableStateFlow<ToolUiState>(ToolUiState.Idle); val toolState = _toolState.asStateFlow()
    private val resolver get() = app.contentResolver

    fun setOnboarded() = viewModelScope.launch { settingsRepo.setOnboarded() }
    fun setTheme(theme: String) = viewModelScope.launch { settingsRepo.setTheme(theme) }
    fun setCompression(level: CompressionLevel) = viewModelScope.launch { settingsRepo.setCompression(level) }
    fun clearHistory() = viewModelScope.launch { historyRepo.clear() }
    fun setInputs(uris: List<Uri>) { val selected = uris.map { SelectedFile(it, resolver.displayName(it), resolver.sizeBytes(it)) }; _toolState.value = ToolUiState.Ready(inputs = selected) }
    fun updateReady(transform: (ToolUiState.Ready) -> ToolUiState.Ready) { val ready = _toolState.value as? ToolUiState.Ready ?: ToolUiState.Ready(); _toolState.value = transform(ready) }

    fun run(tool: PdfToolType) = viewModelScope.launch {
        val ready = _toolState.value as? ToolUiState.Ready ?: return@launch
        val plan = settings.value.userPlan
        try {
            when (tool) {
                PdfToolType.COMPRESS -> { val file = ready.inputs.firstOrNull() ?: return@launch fail("Bitte wähle eine PDF-Datei aus."); if(!limits.canProcessPdf(file.sizeBytes, plan)) return@launch fail("Die Datei ist zu groß für die kostenlose Version."); process("PDF wird komprimiert…") { engine.compress(file.uri, ready.level) } }
                PdfToolType.MERGE -> { if(!limits.canMerge(ready.inputs.size, plan)) return@launch fail("Kostenlos kannst du bis zu 5 PDFs zusammenführen."); process("PDFs werden zusammengeführt…") { engine.merge(ready.inputs.map { it.uri }) } }
                PdfToolType.SPLIT -> { val file = ready.inputs.firstOrNull() ?: return@launch fail("Bitte wähle eine PDF-Datei aus."); process("Seiten werden extrahiert…") { engine.split(file.uri, ready.range).first() } }
                PdfToolType.IMAGES_TO_PDF -> { if(!limits.canUseImages(ready.inputs.size, plan)) return@launch fail("Kostenlos kannst du bis zu 10 Bilder nutzen."); process("PDF aus Bildern wird erstellt…") { engine.imagesToPdf(ready.inputs.map { it.uri }) } }
                PdfToolType.ROTATE -> { val file = ready.inputs.firstOrNull() ?: return@launch fail("Bitte wähle eine PDF-Datei aus."); process("Seiten werden gedreht…") { engine.rotate(file.uri, ready.rotation) } }
                PdfToolType.PROTECT -> { val file = ready.inputs.firstOrNull() ?: return@launch fail("Bitte wähle eine PDF-Datei aus."); if(ready.password.length < 4) return@launch fail("Bitte gib ein Passwort mit mindestens 4 Zeichen ein."); process("PDF wird geschützt…") { engine.protect(file.uri, ready.password, ready.allowPrint, ready.allowCopy) } }
                PdfToolType.UNLOCK -> { val file = ready.inputs.firstOrNull() ?: return@launch fail("Bitte wähle eine PDF-Datei aus."); process("PDF wird entsperrt…") { engine.unlock(file.uri, ready.password) } }
                else -> fail("Dieses Werkzeug ist im MVP als Vorschau enthalten.")
            }
        } catch (e: Exception) { _toolState.value = ToolUiState.Error(e.message ?: "Diese PDF ist beschädigt oder kann nicht gelesen werden.") }
    }
    private fun fail(message: String) { _toolState.value = ToolUiState.Error(message) }
    private suspend fun process(text: String, block: suspend () -> PdfResult) { _toolState.value = ToolUiState.Processing(text); val result = block(); historyRepo.add(HistoryItem(toolType = PdfToolType.PREVIEW, displayName = result.fileName, createdAt = System.currentTimeMillis(), outputSizeBytes = result.outputSizeBytes, inputSizeBytes = result.inputSizeBytes, outputUriString = result.uri.toString())); _toolState.value = ToolUiState.Done(result) }
}
