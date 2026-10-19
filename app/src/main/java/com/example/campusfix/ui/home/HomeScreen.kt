package com.example.campusfix.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.theme.*

@Composable
fun HomeScreen(
    onReportProblem: () -> Unit,
    onViewAll: () -> Unit,
    onBrowseCampus: () -> Unit,
    onOpenReport: (String) -> Unit,
    onActiveClick: () -> Unit = {},
    onResolvedClick: () -> Unit = {}
) {
    val mine = MockReportRepository.myReports
    val active = mine.count { it.isOpen }
    val resolved = mine.count { !it.isOpen }
    val campusOpen = MockReportRepository.reports.count { it.isOpen }
    val shape = RoundedCornerShape(16.dp)

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Good morning \uD83D\uDC4B", fontSize = 13.sp, color = CfMuted)
        Text("What needs fixing?", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CfText)
        Text(
            "CampusFix lets you report broken facilities on campus and follow each repair until it's fixed.",
            fontSize = 13.sp, color = CfMuted, modifier = Modifier.padding(top = 4.dp)
        )

        // 1. Main action
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth().clip(shape).background(CfGreen)
                .clickable(onClick = onReportProblem).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("REPORT A PROBLEM", color = CfOnGreen.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("Something on campus isn't working properly?", color = CfOnGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Takes about a minute. Photo optional.", color = CfOnGreen.copy(alpha = 0.85f), fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = CfOnGreen)
        }

        // 2. How it works
        Spacer(Modifier.height(22.dp))
        SectionTitle("How it works")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StepTile("1", "Report", "Describe the issue and where it is", Modifier.weight(1f))
            StepTile("2", "Assigned", "A maintenance officer picks it up", Modifier.weight(1f))
            StepTile("3", "Fixed", "Track progress until resolved", Modifier.weight(1f))
        }

        // 3. Check first (duplicate avoidance)
        Spacer(Modifier.height(22.dp))
        Row(
            Modifier.fillMaxWidth().clip(shape).background(CfSurface)
                .clickable(onClick = onBrowseCampus).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(40.dp).background(CfMint, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Search, null, tint = CfGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Already reported? Check first", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CfText)
                Text("$campusOpen open issues on campus. If yours is listed, add yourself instead of reporting again.", fontSize = 12.sp, color = CfMuted)
            }
            Icon(Icons.Default.KeyboardArrowRight, null, tint = CfMuted)
        }

        // 4. Your reports
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Your reports", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CfText, modifier = Modifier.weight(1f))
            if (mine.isNotEmpty()) TextButton(onClick = onViewAll) { Text("View all", color = CfGreen, fontSize = 12.sp) }
        }
        if (mine.isEmpty()) {
            Text("You haven't reported anything yet. Tap the green card above to start.", fontSize = 13.sp, color = CfMuted)
        } else {
            mine.take(2).forEach {
                ReportCard(it, showId = false) { onOpenReport(it.id) }
                Spacer(Modifier.height(10.dp))
            }
        }

        // 5. Quick info
        Spacer(Modifier.height(14.dp))
        SectionTitle("Quick info")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("$active", "Active reports", "Being handled", Modifier.weight(1f), onActiveClick)
            StatCard("$resolved", "Resolved", "Recently completed", Modifier.weight(1f), onResolvedClick)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CfText, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun StepTile(number: String, title: String, text: String, modifier: Modifier) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CfSurface),
        border = BorderStroke(1.dp, CfBorder)
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(Modifier.size(24.dp).background(CfMint, CircleShape), contentAlignment = Alignment.Center) {
                Text(number, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CfGreen)
            }
            Spacer(Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = CfText)
            Text(text, fontSize = 11.sp, color = CfMuted)
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, caption: String, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CfSurface),
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
