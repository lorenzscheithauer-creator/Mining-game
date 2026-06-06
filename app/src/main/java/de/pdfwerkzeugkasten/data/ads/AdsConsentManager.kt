package de.pdfwerkzeugkasten.data.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

class AdsConsentManager(private val context: Context) {
    fun initialize(activity: Activity, onDone: () -> Unit = {}) {
        val info = UserMessagingPlatform.getConsentInformation(context)
        info.requestConsentInfoUpdate(activity, ConsentRequestParameters.Builder().build(), { UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { MobileAds.initialize(context); onDone() } }, { MobileAds.initialize(context); onDone() })
    }
    fun resetConsent() { UserMessagingPlatform.getConsentInformation(context).reset() }
}
