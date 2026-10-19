package com.example.campusfix.data

import com.example.campusfix.data.model.Report
import com.example.campusfix.data.model.ReportStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportTest {

    private fun report(room: String = "Room 2", status: ReportStatus = ReportStatus.REPORTED) = Report(
        id = "CF-2026-0001", title = "Test", category = "Equipment",
        building = "Mwambo", room = room, description = "desc",
        submittedAt = "now", status = status, reporterId = "me"
    )

    @Test
    fun location_combinesBuildingAndRoom() {
        assertEquals("Mwambo \u2022 Room 2", report(room = "Room 2").location)
    }

    @Test
    fun location_withoutRoom_isJustTheBuilding() {
        assertEquals("Mwambo", report(room = "").location)
    }

    @Test
    fun isOpen_isFalseOnlyWhenResolved() {
        assertTrue(report(status = ReportStatus.REPORTED).isOpen)
        assertTrue(report(status = ReportStatus.ASSIGNED).isOpen)
        assertTrue(report(status = ReportStatus.IN_PROGRESS).isOpen)
        assertFalse(report(status = ReportStatus.RESOLVED).isOpen)
    }

    // The status timeline on the details screen relies on this order.
    @Test
    fun statusOrder_followsTheWorkflow() {
        val order = ReportStatus.entries
        assertEquals(
            listOf(ReportStatus.REPORTED, ReportStatus.ASSIGNED, ReportStatus.IN_PROGRESS, ReportStatus.RESOLVED),
            order
        )
    }
}
