package de.pdfwerkzeugkasten.domain.usecase

class PageRangeParser {
    fun parse(input: String, pageCount: Int): Result<List<Int>> = runCatching {
        require(pageCount > 0) { "PDF enthält keine Seiten." }
        if (input.isBlank()) return@runCatching (1..pageCount).toList()
        val pages = linkedSetOf<Int>()
        input.split(',').map { it.trim() }.filter { it.isNotEmpty() }.forEach { part ->
            if ('-' in part) {
                val bounds = part.split('-', limit = 2).map { it.trim().toInt() }
                require(bounds[0] <= bounds[1]) { "Ungültiger Seitenbereich: $part" }
                (bounds[0]..bounds[1]).forEach { pages += checked(it, pageCount) }
            } else pages += checked(part.toInt(), pageCount)
        }
        pages.toList()
    }
    private fun checked(page: Int, pageCount: Int): Int { require(page in 1..pageCount) { "Seite $page liegt außerhalb von 1-$pageCount." }; return page }
}
