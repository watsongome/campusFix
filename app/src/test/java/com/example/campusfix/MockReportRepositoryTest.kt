package com.example.campusfix.data

import com.example.campusfix.data.model.ReportStatus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MockReportRepositoryTest {

    private val repo = MockReportRepository

    @Before
    fun setUp() {
        Session.signOut()
        repo.reset()
        Session.signIn("student@unima.ac.mw")   // the demo student, id "me"
    }

    @After
    fun tearDown() {
        Session.signOut()
        repo.reset()
    }

    // ---------- add ----------

    @Test
    fun add_createsReportedReportAtTheTop() {
        val created = repo.add("Broken door", "Equipment", "Admin Block", "Room 1", "Door won't close", false)

        assertEquals(created, repo.reports.first())
        assertEquals(ReportStatus.REPORTED, created.status)
        assertEquals("me", created.reporterId)
        assertEquals(1, created.affectedCount)
    }

    @Test
    fun add_generatesSequentialIds() {
        val first = repo.add("A", "Equipment", "Library", "", "x", false)
        val second = repo.add("B", "Equipment", "Library", "", "y", false)

        assertEquals("CF-2026-0045", first.id)
        assertEquals("CF-2026-0046", second.id)
    }

    @Test
    fun add_trimsTextInput() {
        val created = repo.add("  Leak  ", "Plumbing", "Library", "  Room 3 ", "  dripping  ", false)

        assertEquals("Leak", created.title)
        assertEquals("Room 3", created.room)
        assertEquals("dripping", created.description)
    }

    @Test
    fun add_isFindableById() {
        val created = repo.add("Leak", "Plumbing", "Library", "", "dripping", true)

        assertEquals(created, repo.find(created.id))
        assertTrue(repo.find(created.id)!!.hasPhoto)
    }

    @Test
    fun find_unknownId_returnsNull() {
        assertEquals(null, repo.find("CF-0000"))
    }

    // ---------- duplicate detection ----------

    @Test
    fun findSimilar_matchesSameBuildingAndCategory() {
        val ids = repo.findSimilar("Library", "Electrical").map { it.id }
        assertEquals(listOf("CF-2026-0044"), ids)
    }

    @Test
    fun findSimilar_ignoresResolvedReports() {
        // The only Library + Furniture report (damaged desk) is already resolved.
        assertTrue(repo.findSimilar("Library", "Furniture").isEmpty())
    }

    @Test
    fun findSimilar_ignoresOtherBuildingsAndCategories() {
        assertTrue(repo.findSimilar("Mwambo", "Electrical").isEmpty())
        assertTrue(repo.findSimilar("Library", "Plumbing").isEmpty())
    }

    @Test
    fun findSimilar_seesNewlyAddedReports() {
        repo.add("Burst pipe", "Plumbing", "Library", "Ground floor", "water everywhere", false)
        assertEquals(1, repo.findSimilar("Library", "Plumbing").size)
    }

    // ---------- "I'm affected too" ----------

    @Test
    fun join_followsReportAndIncreasesAffectedCount() {
        val before = repo.find("CF-2026-0044")!!.affectedCount

        repo.join("CF-2026-0044")

        val after = repo.find("CF-2026-0044")!!
        assertEquals(before + 1, after.affectedCount)
        assertTrue(repo.isFollowing(after))
        assertTrue(repo.myReports.any { it.id == "CF-2026-0044" })
    }

    @Test
    fun join_twice_countsOnlyOnce() {
        repo.join("CF-2026-0044")
        repo.join("CF-2026-0044")

        assertEquals(6, repo.find("CF-2026-0044")!!.affectedCount)
    }

    @Test
    fun join_ownReport_isIgnored() {
        val before = repo.find("CF-2026-0042")!!.affectedCount

        repo.join("CF-2026-0042")

        assertEquals(before, repo.find("CF-2026-0042")!!.affectedCount)
        assertFalse(repo.isFollowing(repo.find("CF-2026-0042")!!))
    }

    @Test
    fun join_unknownId_doesNothing() {
        repo.join("CF-0000")
        assertEquals(6, repo.reports.size)
    }

    // ---------- my reports ----------

    @Test
    fun myReports_startsWithOnlyTheStudentsOwnReports() {
        val mine = repo.myReports
        assertEquals(3, mine.size)
        assertTrue(mine.all { it.reporterId == "me" })
    }

    @Test
    fun myReports_includesNewlySubmittedReport() {
        val created = repo.add("Broken door", "Equipment", "Admin Block", "", "stuck", false)
        assertNotNull(repo.myReports.firstOrNull { it.id == created.id })
    }

    @Test
    fun officer_hasNoReportsOfTheirOwn() {
        Session.signIn("officer@unima.ac.mw")
        assertTrue(repo.myReports.isEmpty())
    }

    // ---------- reset ----------

    @Test
    fun reset_restoresTheOriginalDemoData() {
        repo.add("Extra", "Other", "Library", "", "x", false)
        repo.join("CF-2026-0044")

        repo.reset()

        assertEquals(6, repo.reports.size)
        assertEquals(5, repo.find("CF-2026-0044")!!.affectedCount)
        assertFalse(repo.isFollowing(repo.find("CF-2026-0044")!!))
        assertEquals("CF-2026-0045", repo.add("Next", "Other", "Library", "", "x", false).id)
    }
}
