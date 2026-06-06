package de.pdfwerkzeugkasten

import de.pdfwerkzeugkasten.domain.usecase.FileNameGenerator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class FileNameGeneratorTest {
    private val generator = FileNameGenerator { LocalDate.of(2026, 6, 6) }
    @Test fun generatesExpectedNames() { assertEquals("compressed_Meine_Datei.pdf", generator.compressed("Meine Datei.pdf")); assertEquals("merged_2026-06-06.pdf", generator.merged()); assertEquals("split_doc_pages_1-3_5.pdf", generator.split("doc.pdf", "1-3, 5")) }
}
