package de.pdfwerkzeugkasten

import android.app.Application
import androidx.room.Room
import de.pdfwerkzeugkasten.data.ads.AdsConsentManager
import de.pdfwerkzeugkasten.data.billing.BillingRepository
import de.pdfwerkzeugkasten.data.history.HistoryDatabase
import de.pdfwerkzeugkasten.data.history.HistoryRepository
import de.pdfwerkzeugkasten.data.pdf.PdfBoxEngine
import de.pdfwerkzeugkasten.data.settings.SettingsRepository

class PdfWerkzeugkastenApp : Application() {
    lateinit var settings: SettingsRepository; lateinit var history: HistoryRepository; lateinit var pdfEngine: PdfBoxEngine; lateinit var billing: BillingRepository; lateinit var ads: AdsConsentManager
    override fun onCreate() { super.onCreate(); settings = SettingsRepository(this); val db = Room.databaseBuilder(this, HistoryDatabase::class.java, "history.db").build(); history = HistoryRepository(db.dao()); pdfEngine = PdfBoxEngine(this); billing = BillingRepository(this, settings); ads = AdsConsentManager(this) }
}
