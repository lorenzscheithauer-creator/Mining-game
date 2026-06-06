package de.pdfwerkzeugkasten

import de.pdfwerkzeugkasten.domain.usecase.PageRangeParser
import org.junit.Assert.*
import org.junit.Test

class PageRangeParserTest {
    private val parser = PageRangeParser()
    @Test fun parsesRangesAndSinglePages() { assertEquals(listOf(1,2,3,5,8,9,10), parser.parse("1-3, 5, 8-10", 12).getOrThrow()) }
    @Test fun rejectsOutOfBoundsPages() { assertTrue(parser.parse("1,9", 3).isFailure) }
}
