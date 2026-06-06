package de.pdfwerkzeugkasten.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.pdfwerkzeugkasten.PdfWerkzeugkastenApp
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory { override fun <T : ViewModel> create(modelClass: Class<T>): T { val app = application as PdfWerkzeugkastenApp; @Suppress("UNCHECKED_CAST") return MainViewModel(application, app.settings, app.history, app.pdfEngine) as T } }
