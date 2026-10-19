package com.example.campusfix.ui.officer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.theme.*

/** Starter officer screen: queue overview. Status updating comes next. */
@Composable
fun OfficerHomeScreen(onOpenReport: (String) -> Unit) {
    val all = MockReportRepository.reports
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
        ScreenHeader("Maintenance queue", "All reports across campus")
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportStatus.entries.forEach { s ->
                Card(
                    Modifier.weight(1f), shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CfSurface),
                    border = BorderStroke(1.dp, CfBorder)
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Text("${all.count { it.status == s }}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CfGreen)
                        Text(s.label, fontSize = 10.sp, color = CfMuted)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(all.sortedByDescending { it.affectedCount }, key = { it.id }) {
                ReportCard(it, showId = true) { onOpenReport(it.id) }
            }
        }
    }
}
