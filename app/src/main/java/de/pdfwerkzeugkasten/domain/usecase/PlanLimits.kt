package de.pdfwerkzeugkasten.domain.usecase

import de.pdfwerkzeugkasten.domain.model.UserPlan

class PlanLimits {
    companion object { const val FREE_MAX_PDF_BYTES = 25L * 1024L * 1024L; const val FREE_MAX_MERGE_COUNT = 5; const val FREE_MAX_IMAGE_COUNT = 10; const val FREE_DAILY_LIMIT = 10 }
    fun canProcessPdf(sizeBytes: Long, plan: UserPlan) = plan == UserPlan.PRO || sizeBytes <= FREE_MAX_PDF_BYTES
    fun canMerge(count: Int, plan: UserPlan) = plan == UserPlan.PRO || count <= FREE_MAX_MERGE_COUNT
    fun canUseImages(count: Int, plan: UserPlan) = plan == UserPlan.PRO || count <= FREE_MAX_IMAGE_COUNT
    fun canStartToday(doneToday: Int, plan: UserPlan) = plan == UserPlan.PRO || doneToday < FREE_DAILY_LIMIT
}
