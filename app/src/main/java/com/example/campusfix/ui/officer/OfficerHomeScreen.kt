package com.example.campusfix.ui.officer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.Report
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.theme.*

/** Officer queue: status tiles double as filters. Open reports first, most affected on top. */
@Composable
fun OfficerHomeScreen(onOpenReport: (String) -> Unit) {
    val all = MockReportRepository.reports
    var selected by rememberSaveable { mutableStateOf(-1) }          // -1 = show everything
    val filter = ReportStatus.entries.getOrNull(selected)

    val visible = all
        .filter { filter == null || it.status == filter }
        .sortedWith(compareByDescending<Report> { it.isOpen }.thenByDescending { it.affectedCount })

    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
        ScreenHeader("Maintenance queue", "All reports across campus")
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportStatus.entries.forEach { status ->
                StatusTile(
                    status = status,
                    count = all.count { it.status == status },
                    selected = filter == status,
                    modifier = Modifier.weight(1f).testTag("filter_${status.name}")
                ) { selected = if (filter == status) -1 else status.ordinal }
            }
        }
        Text(
            if (filter == null) "Tap a status to filter. Most affected first."
            else "Showing ${filter.label} reports. Tap the status again to clear.",
            fontSize = 12.sp, color = CfMuted, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )
        if (visible.isEmpty()) {
            Text("No reports with this status.", color = CfMuted, modifier = Modifier.padding(top = 24.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(visible, key = { it.id }) { ReportCard(it, showId = true) { onOpenReport(it.id) } }
            }
        }
    }
}

@Composable
private fun StatusTile(
    status: ReportStatus, count: Int, selected: Boolean,
    modifier: Modifier, onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = modifier.clip(shape).clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = if (selected) CfMint else CfSurface),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) CfGreen else CfBorder)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text("$count", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CfGreen)
            Text(status.label, fontSize = 10.sp, color = CfMuted)
        }
    }
}
