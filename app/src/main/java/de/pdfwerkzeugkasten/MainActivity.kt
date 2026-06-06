package de.pdfwerkzeugkasten

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import de.pdfwerkzeugkasten.domain.model.PdfToolType
import de.pdfwerkzeugkasten.navigation.Routes
import de.pdfwerkzeugkasten.theme.PdfWerkzeugkastenTheme
import de.pdfwerkzeugkasten.ui.MainViewModel
import de.pdfwerkzeugkasten.ui.MainViewModelFactory
import de.pdfwerkzeugkasten.ui.history.HistoryScreen
import de.pdfwerkzeugkasten.ui.home.HomeScreen
import de.pdfwerkzeugkasten.ui.onboarding.OnboardingScreen
import de.pdfwerkzeugkasten.ui.pro.ProScreen
import de.pdfwerkzeugkasten.ui.settings.PrivacyPolicyScreen
import de.pdfwerkzeugkasten.ui.settings.SettingsScreen
import de.pdfwerkzeugkasten.ui.tools.ToolFlowScreen

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels { MainViewModelFactory(application) }
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); (application as PdfWerkzeugkastenApp).ads.initialize(this); handleShare(intent); setContent { val settings by vm.settings.collectAsStateWithLifecycle(); PdfWerkzeugkastenTheme(settings.theme) { AppNav(vm, settings.onboarded) } } }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); handleShare(intent) }
    private fun handleShare(intent: Intent?) {
        if (intent?.action !in listOf(Intent.ACTION_SEND, Intent.ACTION_SEND_MULTIPLE)) return
        val uris = when (intent.action) {
            Intent.ACTION_SEND -> listOfNotNull(streamUri(intent))
            else -> streamUris(intent)
        }
        if (uris.isNotEmpty()) vm.setInputs(uris)
    }
    @Suppress("DEPRECATION")
    private fun streamUri(intent: Intent): Uri? = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java) else intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
    @Suppress("DEPRECATION")
    private fun streamUris(intent: Intent): List<Uri> = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java) ?: emptyList() else intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM) ?: emptyList()
}

@Composable private fun AppNav(vm: MainViewModel, onboarded: Boolean) {
    val nav = rememberNavController()
    Scaffold(bottomBar = { BottomBar(nav) }) { padding ->
        NavHost(navController = nav, startDestination = if(onboarded) Routes.HOME else Routes.ONBOARDING, modifier = Modifier.padding(padding)) {
            composable(Routes.ONBOARDING) { OnboardingScreen { vm.setOnboarded(); nav.navigate(Routes.HOME) { popUpTo(Routes.ONBOARDING){inclusive=true} } } }
            composable(Routes.HOME) { HomeScreen(onTool = { nav.navigate(Routes.tool(it.name)) }) }
            composable(Routes.TOOL) { backStack -> ToolFlowScreen(PdfToolType.valueOf(backStack.arguments?.getString("type") ?: PdfToolType.COMPRESS.name), vm, onBack = { nav.popBackStack() }) }
            composable(Routes.HISTORY) { HistoryScreen(vm) }
            composable(Routes.PRO) { ProScreen() }
            composable(Routes.SETTINGS) { SettingsScreen(vm, onPrivacy = { nav.navigate(Routes.PRIVACY) }) }
            composable(Routes.PRIVACY) { PrivacyPolicyScreen() }
        }
    }
}
@Composable private fun BottomBar(nav: NavHostController) { NavigationBar { listOf(Routes.HOME to Icons.Default.Home to "Home", Routes.HISTORY to Icons.Default.History to "Verlauf", Routes.PRO to Icons.Default.Star to "Pro", Routes.SETTINGS to Icons.Default.Settings to "Einstellungen").forEach { (pair, label) -> NavigationBarItem(selected=false, onClick={nav.navigate(pair.first)}, icon={Icon(pair.second,null)}, label={Text(label)}) } } }
