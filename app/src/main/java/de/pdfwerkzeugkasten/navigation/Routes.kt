package de.pdfwerkzeugkasten.navigation

object Routes { const val ONBOARDING="onboarding"; const val HOME="home"; const val TOOL="tool/{type}"; const val HISTORY="history"; const val PRO="pro"; const val SETTINGS="settings"; const val PRIVACY="privacy"; fun tool(type: String)="tool/$type" }
