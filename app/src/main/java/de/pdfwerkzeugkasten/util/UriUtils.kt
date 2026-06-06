package de.pdfwerkzeugkasten.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun ContentResolver.displayName(uri: Uri): String = query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null } ?: uri.lastPathSegment?.substringAfterLast('/') ?: "document.pdf"
fun ContentResolver.sizeBytes(uri: Uri): Long = query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { if (it.moveToFirst()) it.getLong(0) else -1L } ?: -1L
fun Long.humanSize(): String { val units = listOf("B","KB","MB","GB"); var v = toDouble().coerceAtLeast(0.0); var i=0; while(v>=1024 && i<units.lastIndex){v/=1024;i++}; return if(i==0) "${v.toLong()} ${units[i]}" else "%.1f %s".format(v, units[i]) }
fun File.nonColliding(): File { if (!exists()) return this; val base = nameWithoutExtension; val ext = extension.let { if (it.isBlank()) "" else ".$it" }; var n=1; var f: File; do { f = File(parentFile, "${base}_$n$ext"); n++ } while(f.exists()); return f }
