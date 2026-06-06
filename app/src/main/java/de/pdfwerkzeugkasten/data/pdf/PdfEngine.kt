package de.pdfwerkzeugkasten.data.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import com.tom_roush.pdfbox.multipdf.Splitter
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import de.pdfwerkzeugkasten.domain.model.CompressionLevel
import de.pdfwerkzeugkasten.domain.model.PdfResult
import de.pdfwerkzeugkasten.domain.usecase.CompressionPolicy
import de.pdfwerkzeugkasten.domain.usecase.FileNameGenerator
import de.pdfwerkzeugkasten.domain.usecase.PageRangeParser
import de.pdfwerkzeugkasten.util.displayName
import de.pdfwerkzeugkasten.util.nonColliding
import de.pdfwerkzeugkasten.util.sizeBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

interface PdfEngine {
    suspend fun compress(input: Uri, level: CompressionLevel): PdfResult
    suspend fun merge(inputs: List<Uri>): PdfResult
    suspend fun split(input: Uri, ranges: String): List<PdfResult>
    suspend fun imagesToPdf(inputs: List<Uri>): PdfResult
    suspend fun rotate(input: Uri, degrees: Int): PdfResult
    suspend fun protect(input: Uri, password: String, allowPrint: Boolean, allowCopy: Boolean): PdfResult
    suspend fun unlock(input: Uri, password: String): PdfResult
    suspend fun pageCount(input: Uri, password: String? = null): Int
}

class PdfBoxEngine(private val context: Context) : PdfEngine {
    private val names = FileNameGenerator()
    private val resolver get() = context.contentResolver
    private fun outFile(name: String): File = File(context.cacheDir, name).nonColliding()
    private fun load(uri: Uri, password: String? = null): PDDocument = resolver.openInputStream(uri).use { input -> if (password == null) PDDocument.load(requireNotNull(input)) else PDDocument.load(requireNotNull(input), password) }

    override suspend fun compress(input: Uri, level: CompressionLevel) = withContext(Dispatchers.IO) {
        val original = resolver.displayName(input); val output = outFile(names.compressed(original)); val inSize = resolver.sizeBytes(input)
        val options = CompressionPolicy().options(level)
        load(input).use { doc ->
            if (options.removeMetadata) doc.documentInformation.keywords = null
            doc.save(output)
        }
        PdfResult(output.name, Uri.fromFile(output), inSize, output.length(), pageCount(Uri.fromFile(output)))
    }

    override suspend fun merge(inputs: List<Uri>) = withContext(Dispatchers.IO) {
        val output = outFile(names.merged()); val merger = PDFMergerUtility(); var inputSize = 0L
        inputs.forEach { uri -> inputSize += resolver.sizeBytes(uri).coerceAtLeast(0); resolver.openInputStream(uri)?.use { stream -> merger.addSource(stream) } }
        FileOutputStream(output).use { merger.destinationStream = it; merger.mergeDocuments(null) }
        PdfResult(output.name, Uri.fromFile(output), inputSize, output.length(), pageCount(Uri.fromFile(output)))
    }

    override suspend fun split(input: Uri, ranges: String) = withContext(Dispatchers.IO) {
        val original = resolver.displayName(input); val doc = load(input); doc.use {
            val pages = PageRangeParser().parse(ranges, doc.numberOfPages).getOrThrow().toSet()
            val target = PDDocument(); target.use { outDoc -> pages.forEach { page -> outDoc.importPage(doc.getPage(page - 1)) }; val output = outFile(names.split(original, ranges.ifBlank { "all" })); outDoc.save(output); listOf(PdfResult(output.name, Uri.fromFile(output), resolver.sizeBytes(input), output.length(), pages.size)) }
        }
    }

    override suspend fun imagesToPdf(inputs: List<Uri>) = withContext(Dispatchers.IO) {
        val output = outFile(names.imagesToPdf()); val doc = PdfDocument(); var inputSize = 0L
        inputs.forEachIndexed { index, uri ->
            inputSize += resolver.sizeBytes(uri).coerceAtLeast(0)
            val bitmap = resolver.openInputStream(uri).use { stream -> BitmapFactory.decodeStream(requireNotNull(stream)) } ?: return@forEachIndexed
            val max = 1600f; val scale = minOf(1f, max / maxOf(bitmap.width, bitmap.height).toFloat())
            val w = (bitmap.width * scale).toInt().coerceAtLeast(1); val h = (bitmap.height * scale).toInt().coerceAtLeast(1)
            val page = doc.startPage(PdfDocument.PageInfo.Builder(w, h, index + 1).create())
            val scaled = Bitmap.createScaledBitmap(bitmap, w, h, true); page.canvas.drawBitmap(scaled, 0f, 0f, null); doc.finishPage(page)
            if (scaled != bitmap) scaled.recycle(); bitmap.recycle()
        }
        FileOutputStream(output).use(doc::writeTo); doc.close(); PdfResult(output.name, Uri.fromFile(output), inputSize, output.length(), inputs.size)
    }

    override suspend fun rotate(input: Uri, degrees: Int) = withContext(Dispatchers.IO) { val original = resolver.displayName(input); val output = outFile(names.rotated(original)); load(input).use { doc -> for (i in 0 until doc.numberOfPages) doc.getPage(i).rotation = (doc.getPage(i).rotation + degrees) % 360; doc.save(output); PdfResult(output.name, Uri.fromFile(output), resolver.sizeBytes(input), output.length(), doc.numberOfPages) } }
    override suspend fun protect(input: Uri, password: String, allowPrint: Boolean, allowCopy: Boolean) = withContext(Dispatchers.IO) { val original = resolver.displayName(input); val output = outFile(names.protected(original)); load(input).use { doc -> val ap = AccessPermission().apply { setCanPrint(allowPrint); setCanExtractContent(allowCopy) }; doc.protect(StandardProtectionPolicy(password, password, ap).apply { encryptionKeyLength = 128 }); doc.save(output); PdfResult(output.name, Uri.fromFile(output), resolver.sizeBytes(input), output.length(), doc.numberOfPages) } }
    override suspend fun unlock(input: Uri, password: String) = withContext(Dispatchers.IO) { val original = resolver.displayName(input); val output = outFile(names.unlocked(original)); load(input, password).use { doc -> doc.setAllSecurityToBeRemoved(true); doc.save(output); PdfResult(output.name, Uri.fromFile(output), resolver.sizeBytes(input), output.length(), doc.numberOfPages) } }
    override suspend fun pageCount(input: Uri, password: String?) = withContext(Dispatchers.IO) { load(input, password).use { it.numberOfPages } }
}
