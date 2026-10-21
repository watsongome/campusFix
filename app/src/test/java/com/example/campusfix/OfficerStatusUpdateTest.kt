package com.example.campusfix.data

import com.example.campusfix.data.model.ReportStatus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// Officer workflow
class OfficerStatusUpdateTest {

    private val repo = MockReportRepository

    // Seed data used below:
    //   CF-2026-0037 Leaking tap     REPORTED,    no assignee
    //   CF-2026-0042 Projector       IN_PROGRESS, assigned to "Officer Gome"
    //   CF-2026-0044 Flickering      ASSIGNED,    assigned to "Officer Gome"
    //   CF-2026-0029 Damaged desk    RESOLVED

    @Before
    fun setUp() {
        Session.signOut()
        repo.reset()
        Session.signIn("officer@unima.ac.mw")        // officer named "Officer"
    }

    @After
    fun tearDown() {
        Session.signOut()
        repo.reset()
    }

    @Test
    fun officer_movesReportOneStepForward() {
        val ok = repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED)

        assertTrue(ok)
        assertEquals(ReportStatus.ASSIGNED, repo.find("CF-2026-0037")!!.status)
    }

    @Test
    fun firstUpdate_recordsTheOfficerAsAssignee() {
        assertNull(repo.find("CF-2026-0037")!!.assignedTo)

        repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED)

        assertEquals("Officer", repo.find("CF-2026-0037")!!.assignedTo)
    }

    @Test
    fun laterUpdates_keepTheExistingAssignee() {
        repo.updateStatus("CF-2026-0042", ReportStatus.RESOLVED)

        assertEquals("Officer Gome", repo.find("CF-2026-0042")!!.assignedTo)
    }

    @Test
    fun cannotSkipSteps() {
        val ok = repo.updateStatus("CF-2026-0037", ReportStatus.RESOLVED)

        assertFalse(ok)
        assertEquals(ReportStatus.REPORTED, repo.find("CF-2026-0037")!!.status)
    }

    @Test
    fun cannotMoveBackwards() {
        assertFalse(repo.updateStatus("CF-2026-0042", ReportStatus.ASSIGNED))
        assertFalse(repo.updateStatus("CF-2026-0042", ReportStatus.REPORTED))
        assertEquals(ReportStatus.IN_PROGRESS, repo.find("CF-2026-0042")!!.status)
    }

    @Test
    fun sameStatus_isRejected() {
        assertFalse(repo.updateStatus("CF-2026-0042", ReportStatus.IN_PROGRESS))
    }

    @Test
    fun resolvedReport_isFinal() {
        ReportStatus.entries.forEach { status ->
            assertFalse(repo.updateStatus("CF-2026-0029", status))
        }
        assertEquals(ReportStatus.RESOLVED, repo.find("CF-2026-0029")!!.status)
    }

    // Security rule: only officers can change statuses.
    @Test
    fun student_cannotUpdateStatus() {
        Session.signIn("student@unima.ac.mw")

        val ok = repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED)

        assertFalse(ok)
        assertEquals(ReportStatus.REPORTED, repo.find("CF-2026-0037")!!.status)
    }

    @Test
    fun signedOutUser_cannotUpdateStatus() {
        Session.signOut()

        assertFalse(repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED))
        assertEquals(ReportStatus.REPORTED, repo.find("CF-2026-0037")!!.status)
    }

    @Test
    fun unknownReport_isRejected() {
        assertFalse(repo.updateStatus("CF-0000", ReportStatus.ASSIGNED))
    }

    @Test
    fun fullWorkflow_reachesResolved() {
        assertTrue(repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED))
        assertTrue(repo.updateStatus("CF-2026-0037", ReportStatus.IN_PROGRESS))
        assertTrue(repo.updateStatus("CF-2026-0037", ReportStatus.RESOLVED))

        assertEquals(ReportStatus.RESOLVED, repo.find("CF-2026-0037")!!.status)
        assertFalse(repo.find("CF-2026-0037")!!.isOpen)
    }

    @Test
    fun resolvedReport_noLongerCountsAsADuplicate() {
        assertEquals(1, repo.findSimilar("Library", "Electrical").size)

        repo.updateStatus("CF-2026-0044", ReportStatus.IN_PROGRESS)
        repo.updateStatus("CF-2026-0044", ReportStatus.RESOLVED)

        assertTrue(repo.findSimilar("Library", "Electrical").isEmpty())
    }

    @Test
    fun reporter_seesTheOfficersUpdateInMyReports() {
        repo.updateStatus("CF-2026-0037", ReportStatus.ASSIGNED)

        Session.signIn("student@unima.ac.mw")

        val mine = repo.myReports.first { it.id == "CF-2026-0037" }
        assertEquals(ReportStatus.ASSIGNED, mine.status)
    }
}
