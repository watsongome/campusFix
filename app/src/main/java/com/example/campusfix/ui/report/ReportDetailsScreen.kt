package com.example.campusfix.ui.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.Session
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.data.model.UserRole
import com.example.campusfix.ui.components.PrimaryButton
import com.example.campusfix.ui.components.SecondaryButton
import com.example.campusfix.ui.components.StatusChip
import com.example.campusfix.ui.theme.*

@Composable
fun ReportDetailsScreen(reportId: String, onBack: () -> Unit) {
    val report = MockReportRepository.find(reportId)

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            Text("Report details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CfText)
        }
        if (report == null) {
            Text("Report not found.", color = CfMuted, modifier = Modifier.padding(20.dp))
            return@Column
        }

        val mine = MockReportRepository.isMine(report)
        val following = MockReportRepository.isFollowing(report)
        val isUser = Session.user?.role != UserRole.OFFICER

        Spacer(Modifier.height(8.dp))
        Card(
            Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CfSurface),
            border = BorderStroke(1.dp, CfBorder)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(report.id, fontSize = 11.sp, color = CfMuted, modifier = Modifier.weight(1f))
                    StatusChip(report.status)
                }
                Spacer(Modifier.height(4.dp))
                Text(report.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = CfText)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${report.affectedCount} ${if (report.affectedCount == 1) "person" else "people"} affected",
                    fontSize = 12.sp, color = CfMuted
                )
            }
        }

        // Someone else's report: join it instead of filing a duplicate
        if (isUser && !mine) {
            Spacer(Modifier.height(12.dp))
            if (following) {
                Text(
                    "\u2713 You're following this report. It now shows in My reports so you can track its progress.",
                    fontSize = 13.sp, color = CfGreen, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth().background(CfMint, RoundedCornerShape(12.dp)).padding(14.dp)
                )
            } else {
                Text("Someone has already reported this. Have the same problem? Add yourself instead of reporting it again.",
                    fontSize = 13.sp, color = CfMuted)
                Spacer(Modifier.height(10.dp))
                PrimaryButton("I'm affected too") { MockReportRepository.join(report.id) }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Status", fontWeight = FontWeight.Bold, color = CfText)
        Spacer(Modifier.height(10.dp))
        ReportStatus.entries.forEachIndexed { index, step ->
            val done = step.ordinal <= report.status.ordinal
            val last = index == ReportStatus.entries.lastIndex
            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier.size(20.dp)
                            .background(if (done) CfGreen else CfSurface, CircleShape)
                            .border(1.dp, if (done) CfGreen else CfBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { if (done) Icon(Icons.Default.Check, null, tint = CfOnGreen, modifier = Modifier.size(12.dp)) }
                    if (!last) Box(Modifier.width(2.dp).height(18.dp).background(if (done) CfGreen else CfBorder))
                }
                Spacer(Modifier.width(12.dp))
                Text(step.label, fontSize = 13.sp, fontWeight = if (done) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (done) CfText else CfMuted, modifier = Modifier.padding(top = 1.dp))
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Report information", fontWeight = FontWeight.Bold, color = CfText)
        Spacer(Modifier.height(8.dp))
        InfoRow("Category", report.category)
        InfoRow("Location", report.location)
        InfoRow("Submitted", report.submittedAt)
        if (report.hasPhoto) InfoRow("Photo", "1 attached")

        Spacer(Modifier.height(12.dp))
        Text("Description", fontWeight = FontWeight.Bold, color = CfText)
        Text(report.description, fontSize = 13.sp, color = CfMuted, modifier = Modifier.padding(top = 4.dp))
        Spacer(Modifier.height(24.dp))
        SecondaryButton("Back", onClick = onBack)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(bottom = 8.dp)) {
        Text(label, fontSize = 11.sp, color = CfMuted)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = CfText)
    }
}
