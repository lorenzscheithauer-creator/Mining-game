package de.pdfwerkzeugkasten.data.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import de.pdfwerkzeugkasten.domain.model.CompressionLevel
import de.pdfwerkzeugkasten.domain.model.UserPlan
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")
data class AppSettings(val onboarded: Boolean = false, val theme: String = "System", val defaultCompression: CompressionLevel = CompressionLevel.MEDIUM, val userPlan: UserPlan = UserPlan.FREE, val adsConsentManaged: Boolean = false)
class SettingsRepository(private val context: Context) {
    private object Keys { val ONBOARDED = booleanPreferencesKey("onboarded"); val THEME = stringPreferencesKey("theme"); val COMPRESSION = stringPreferencesKey("compression"); val PLAN = stringPreferencesKey("plan"); val CONSENT = booleanPreferencesKey("consent") }
    val settings = context.dataStore.data.map { p -> AppSettings(p[Keys.ONBOARDED] ?: false, p[Keys.THEME] ?: "System", CompressionLevel.valueOf(p[Keys.COMPRESSION] ?: CompressionLevel.MEDIUM.name), UserPlan.valueOf(p[Keys.PLAN] ?: UserPlan.FREE.name), p[Keys.CONSENT] ?: false) }
    suspend fun setOnboarded() = context.dataStore.edit { it[Keys.ONBOARDED] = true }
    suspend fun setTheme(theme: String) = context.dataStore.edit { it[Keys.THEME] = theme }
    suspend fun setCompression(level: CompressionLevel) = context.dataStore.edit { it[Keys.COMPRESSION] = level.name }
    suspend fun setPlan(plan: UserPlan) = context.dataStore.edit { it[Keys.PLAN] = plan.name }
    suspend fun setConsentManaged(value: Boolean) = context.dataStore.edit { it[Keys.CONSENT] = value }
}
