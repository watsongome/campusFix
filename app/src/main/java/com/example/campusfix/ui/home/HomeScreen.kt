package com.example.campusfix.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.theme.*

@Composable
fun HomeScreen(onReportProblem: () -> Unit, onViewAll: () -> Unit, onOpenReport: (String) -> Unit) {
    val reports = MockReportRepository.reports
    val active = reports.count { it.status != ReportStatus.RESOLVED }
    val resolved = reports.count { it.status == ReportStatus.RESOLVED }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        //Text("Good morning", fontSize = 13.sp, color = CfMuted)
        Text("What needs fixing?", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CfText)
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(CfGreen)
                .clickable(onClick = onReportProblem).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("REPORT A PROBLEM", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("Something on campus isn't working properly?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
        }

        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Your reports", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CfText, modifier = Modifier.weight(1f))
            TextButton(onClick = onViewAll) { Text("View all", color = CfGreen, fontSize = 12.sp) }
        }
        reports.take(2).forEach {
            ReportCard(it, showId = false) { onOpenReport(it.id) }
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(14.dp))
        Text("Quick info", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CfText)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("$active", "Active reports", "Being handled", Modifier.weight(1f))
            StatCard("$resolved", "Resolved", "Recently completed", Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, caption: String, modifier: Modifier) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CfBorder)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CfGreen)
            Text(label, fontSize = 12.sp, color = CfMuted)
            Spacer(Modifier.height(6.dp))
            Text(caption, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CfText)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() = CampusFixTheme {
    HomeScreen(onReportProblem = {}, onViewAll = {}, onOpenReport = {})
}