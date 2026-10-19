package com.example.campusfix.data

import androidx.compose.runtime.mutableStateListOf
import com.example.campusfix.data.model.Report
import com.example.campusfix.data.model.ReportStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** In-memory fake data. Replace with Supabase later; screens only talk to these functions. */
object MockReportRepository {

    private fun seedReports() = listOf(
        // ...your existing 6 Report(...) entries, unchanged...
        Report("CF-2026-0044", "Flickering lights", "Electrical", "Library", "1st Floor",
            "The lights above the reading area flicker all day.", "02 Oct 2026, 15:40",
            ReportStatus.ASSIGNED, reporterId = "u2", affectedCount = 5),
        Report("CF-2026-0042", "Broken classroom projector", "Equipment", "Mwambo", "Room 2",
            "The projector turns on but shows no image. Maintenance has started checking the issue.",
            "02 Oct 2026, 10:24", ReportStatus.IN_PROGRESS, reporterId = "me", affectedCount = 2),
        Report("CF-2026-0041", "Blocked drain", "Plumbing", "Chilunga", "Ground floor",
            "Water collects outside the washrooms after it rains.", "01 Oct 2026, 08:15",
            ReportStatus.REPORTED, reporterId = "u3", affectedCount = 4),
        Report("CF-2026-0039", "Broken window", "Building / Structure", "Science Block", "Lab 3",
            "A window pane is cracked and may fall out.", "30 Sep 2026, 16:30",
            ReportStatus.IN_PROGRESS, reporterId = "u4", affectedCount = 2),
        Report("CF-2026-0037", "Leaking tap", "Plumbing", "Chilunga", "Room 38",
            "The tap in the corridor washroom keeps dripping and floods the floor.",
            "30 Sep 2026, 14:10", ReportStatus.REPORTED, reporterId = "me"),
        Report("CF-2026-0029", "Damaged desk", "Furniture", "Library", "1st Floor",
            "One desk has a broken leg and wobbles badly.", "24 Sep 2026, 09:05",
            ReportStatus.RESOLVED, reporterId = "me")
    )

    val reports = mutableStateListOf<Report>().apply { addAll(seedReports()) }

    fun reset() {
        reports.clear()
        reports.addAll(seedReports())
        followedIds.clear()
        nextNumber = 45
    }


    /** Reports the current user is "affected too" by (someone else's report they follow). */
    private val followedIds = mutableStateListOf<String>()

    private var nextNumber = 45

    private val myId: String get() = Session.user?.id ?: "me"

    val myReports: List<Report>
        get() = reports.filter { it.reporterId == myId || it.id in followedIds }

    fun isMine(report: Report) = report.reporterId == myId
    fun isFollowing(report: Report) = report.id in followedIds

    /** Open reports in the same building and category: the likely duplicates. */
    fun findSimilar(building: String, category: String): List<Report> =
        reports.filter { it.isOpen && it.building == building && it.category == category }

    /** "I'm affected too": follow an existing report instead of filing a duplicate. */
    fun join(id: String) {
        val index = reports.indexOfFirst { it.id == id }
        if (index < 0) return
        val report = reports[index]
        if (report.reporterId == myId || id in followedIds) return
        followedIds.add(id)
        reports[index] = report.copy(affectedCount = report.affectedCount + 1)
    }

    fun add(
        title: String, category: String, building: String, room: String,
        description: String, hasPhoto: Boolean
    ): Report {
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        val report = Report(
            id = "CF-2026-%04d".format(nextNumber++),
            title = title.trim(), category = category,
            building = building, room = room.trim(),
            description = description.trim(), submittedAt = now,
            status = ReportStatus.REPORTED, reporterId = myId, hasPhoto = hasPhoto
        )
        reports.add(0, report)
        return report
    }

    fun find(id: String): Report? = reports.firstOrNull { it.id == id }
}
