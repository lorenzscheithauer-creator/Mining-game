package de.pdfwerkzeugkasten.domain.usecase

import de.pdfwerkzeugkasten.domain.model.CompressionLevel

data class CompressionOptions(val imageScale: Float, val jpegQuality: Int, val removeMetadata: Boolean)
class CompressionPolicy { fun options(level: CompressionLevel) = when(level) { CompressionLevel.LIGHT -> CompressionOptions(1f, 92, false); CompressionLevel.MEDIUM -> CompressionOptions(.82f, 76, true); CompressionLevel.STRONG -> CompressionOptions(.62f, 55, true) } }
