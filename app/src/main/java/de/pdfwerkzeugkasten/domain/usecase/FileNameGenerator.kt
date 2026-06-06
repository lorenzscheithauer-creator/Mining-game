package de.pdfwerkzeugkasten.domain.usecase

import java.text.Normalizer
import java.time.LocalDate

class FileNameGenerator(private val today: () -> LocalDate = { LocalDate.now() }) {
    fun compressed(original: String) = "compressed_${safeBase(original)}.pdf"
    fun merged() = "merged_${today()}.pdf"
    fun imagesToPdf() = "images_to_pdf_${today()}.pdf"
    fun split(original: String, range: String) = "split_${safeBase(original)}_pages_${range.replace(",", "_").replace(" ", "")}.pdf"
    fun rotated(original: String) = "rotated_${safeBase(original)}.pdf"
    fun protected(original: String) = "protected_${safeBase(original)}.pdf"
    fun unlocked(original: String) = "unlocked_${safeBase(original)}.pdf"
    private fun safeBase(name: String): String = Normalizer.normalize(name.substringBeforeLast('.'), Normalizer.Form.NFKD).replace(Regex("[^A-Za-z0-9_-]+"), "_").trim('_').ifBlank { "document" }
}
