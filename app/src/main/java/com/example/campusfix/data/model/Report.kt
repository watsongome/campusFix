package com.example.campusfix.data.model

enum class ReportStatus(val label: String) {
    REPORTED("Reported"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved")
}

data class Report(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val description: String,
    val submittedAt: String,
    val status: ReportStatus,
    val hasPhoto: Boolean = false
)

val reportCategories = listOf(
    "Equipment", "Furniture", "Electrical", "Plumbing", "Building / Structure", "Other"
)
