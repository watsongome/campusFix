package com.example.campusfix.data

import androidx.compose.runtime.mutableStateListOf
import com.example.campusfix.data.model.Report
import com.example.campusfix.data.model.ReportStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// In-memory fake data. Supabase
object MockReportRepository {

    val reports = mutableStateListOf(
        Report(
            "CF-2026-0042", "Broken classroom projector", "Equipment", "Mwambo, Room 2",
            "The projector turns on but shows no image. Maintenance has started checking the issue.",
            "02 Oct 2026, 10:24", ReportStatus.IN_PROGRESS
        ),
        Report(
            "CF-2026-0037", "Leaking tap", "Plumbing", "Chilunga, Room 38",
            "The tap in the corridor washroom keeps dripping and floods the floor.",
            "30 Sep 2026, 14:10", ReportStatus.REPORTED
        ),
        Report(
            "CF-2026-0029", "Damaged desk", "Furniture", "Library, 1st Floor",
            "One desk has a broken leg and wobbles badly.",
            "24 Sep 2026, 09:05", ReportStatus.RESOLVED
        )
    )

    private var nextNumber = 43

    fun add(title: String, category: String, location: String, description: String, hasPhoto: Boolean): Report {
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        val report = Report(
            id = "CF-2026-%04d".format(nextNumber++),
            title = title.trim(), category = category, location = location.trim(),
            description = description.trim(), submittedAt = now,
            status = ReportStatus.REPORTED, hasPhoto = hasPhoto
        )
        reports.add(0, report)
        return report
    }

    fun find(id: String): Report? = reports.firstOrNull { it.id == id }
}
