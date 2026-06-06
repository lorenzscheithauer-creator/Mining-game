package de.pdfwerkzeugkasten

import de.pdfwerkzeugkasten.domain.model.CompressionLevel
import de.pdfwerkzeugkasten.domain.usecase.CompressionPolicy
import org.junit.Assert.*
import org.junit.Test

class CompressionPolicyTest { @Test fun strongerCompressionUsesLowerQuality() { val p = CompressionPolicy(); assertTrue(p.options(CompressionLevel.LIGHT).jpegQuality > p.options(CompressionLevel.MEDIUM).jpegQuality); assertTrue(p.options(CompressionLevel.MEDIUM).jpegQuality > p.options(CompressionLevel.STRONG).jpegQuality); assertTrue(p.options(CompressionLevel.STRONG).removeMetadata) } }
