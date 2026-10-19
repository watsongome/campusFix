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
    val building: String,
    val room: String,
    val description: String,
    val submittedAt: String,
    val status: ReportStatus,
    val reporterId: String,
    val affectedCount: Int = 1,
    val hasPhoto: Boolean = false
) {
    val location: String get() = if (room.isBlank()) building else "$building \u2022 $room"
    val isOpen: Boolean get() = status != ReportStatus.RESOLVED
}

val reportCategories = listOf(
    "Equipment", "Furniture", "Electrical", "Plumbing", "Building / Structure", "Other"
)

// Replace with your real campus buildings later (or load them from the backend).
val campusBuildings = listOf(
    "Mwambo", "Chilunga", "Library", "Science Block", "Student Hostel", "Cafeteria", "Admin Block", "Other"
)
