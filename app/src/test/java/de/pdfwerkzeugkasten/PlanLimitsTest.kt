package de.pdfwerkzeugkasten

import de.pdfwerkzeugkasten.domain.model.UserPlan
import de.pdfwerkzeugkasten.domain.usecase.PlanLimits
import org.junit.Assert.*
import org.junit.Test

class PlanLimitsTest {
    private val limits = PlanLimits()
    @Test fun freeLimitsApply() { assertTrue(limits.canProcessPdf(25L*1024*1024, UserPlan.FREE)); assertFalse(limits.canProcessPdf(26L*1024*1024, UserPlan.FREE)); assertFalse(limits.canMerge(6, UserPlan.FREE)); assertFalse(limits.canUseImages(11, UserPlan.FREE)); assertFalse(limits.canStartToday(10, UserPlan.FREE)) }
    @Test fun proIsUnlimitedForConfiguredLimits() { assertTrue(limits.canProcessPdf(Long.MAX_VALUE, UserPlan.PRO)); assertTrue(limits.canMerge(500, UserPlan.PRO)); assertTrue(limits.canUseImages(500, UserPlan.PRO)) }
}
