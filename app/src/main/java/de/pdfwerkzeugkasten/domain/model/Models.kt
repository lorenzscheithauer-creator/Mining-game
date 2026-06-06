package de.pdfwerkzeugkasten.domain.model

import android.net.Uri

enum class PdfToolType { COMPRESS, MERGE, SPLIT, IMAGES_TO_PDF, PDF_TO_IMAGES, ROTATE, PROTECT, UNLOCK, PREVIEW, HISTORY }
enum class CompressionLevel(val title: String, val jpegQuality: Int) { LIGHT("Leicht", 92), MEDIUM("Mittel", 76), STRONG("Stark", 55) }
enum class UserPlan { FREE, PRO }
enum class JobStatus { QUEUED, RUNNING, SUCCESS, FAILED, CANCELLED }

data class PdfTool(val id: PdfToolType, val title: String, val description: String, val icon: String, val category: String = "PDF", val isProFeature: Boolean = false)
data class PdfJob(val id: String, val type: PdfToolType, val inputUris: List<Uri>, val outputUri: Uri? = null, val status: JobStatus = JobStatus.QUEUED, val progress: Int = 0, val createdAt: Long = System.currentTimeMillis(), val completedAt: Long? = null, val errorMessage: String? = null)
data class HistoryItem(val id: Long = 0, val toolType: PdfToolType, val displayName: String, val createdAt: Long, val outputSizeBytes: Long, val inputSizeBytes: Long, val outputUriString: String? = null)
data class PdfResult(val fileName: String, val uri: Uri, val inputSizeBytes: Long, val outputSizeBytes: Long, val pageCount: Int? = null)
data class SelectedFile(val uri: Uri, val displayName: String, val sizeBytes: Long)
