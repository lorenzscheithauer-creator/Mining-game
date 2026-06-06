package de.pdfwerkzeugkasten.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import de.pdfwerkzeugkasten.data.settings.SettingsRepository
import de.pdfwerkzeugkasten.domain.model.UserPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class PurchaseState { data object Idle: PurchaseState(); data object Success: PurchaseState(); data object Pending: PurchaseState(); data object Cancelled: PurchaseState(); data class Error(val message: String): PurchaseState() }
class BillingRepository(private val context: Context, private val settings: SettingsRepository) : PurchasesUpdatedListener {
    private val _state = MutableStateFlow<PurchaseState>(PurchaseState.Idle); val state: StateFlow<PurchaseState> = _state
    private val client = BillingClient.newBuilder(context).enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()).setListener(this).build()
    fun connect() { if (!client.isReady) client.startConnection(object: BillingClientStateListener { override fun onBillingSetupFinished(r: BillingResult) { if (r.responseCode == BillingClient.BillingResponseCode.OK) restore() else _state.value = PurchaseState.Error("Kauf konnte nicht geprüft werden.") }; override fun onBillingServiceDisconnected() {} }) }
    fun launchPurchase(activity: Activity) { connect(); _state.value = PurchaseState.Error("Produkt-ID lifetime_pro im Play Console Konto anlegen. Testkauf im internen Track aktivieren.") }
    fun restore() { if (!client.isReady) return; client.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()) { _, purchases -> if (purchases.any { p -> p.products.contains("lifetime_pro") && p.purchaseState == Purchase.PurchaseState.PURCHASED }) GlobalScope.launch(Dispatchers.IO) { settings.setPlan(UserPlan.PRO) } } }
    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) { _state.value = when (result.responseCode) { BillingClient.BillingResponseCode.OK -> if (purchases?.any { it.purchaseState == Purchase.PurchaseState.PENDING } == true) PurchaseState.Pending else PurchaseState.Success; BillingClient.BillingResponseCode.USER_CANCELED -> PurchaseState.Cancelled; else -> PurchaseState.Error(result.debugMessage.ifBlank { "Kauf konnte nicht geprüft werden." }) } }
}
