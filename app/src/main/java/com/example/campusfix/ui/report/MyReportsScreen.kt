package com.example.campusfix.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.ui.components.CfTextField
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.theme.*

enum class ReportFilter(val label: String) {
    ALL("All"), ACTIVE("Active"), RESOLVED("Resolved")
}

/** Tab 0 = reports I filed or follow, tab 1 = every report on campus (check here before reporting). */
@Composable
fun MyReportsScreen(
    initialTab: Int = 0,
    initialFilter: ReportFilter = ReportFilter.ALL,
    onOpenReport: (String) -> Unit
) {
    var tab by rememberSaveable { mutableStateOf(initialTab) }
    var filterIndex by rememberSaveable { mutableStateOf(initialFilter.ordinal) }
    var query by rememberSaveable { mutableStateOf("") }
    val filter = ReportFilter.entries[filterIndex]

    val source = if (tab == 0) MockReportRepository.myReports
    else MockReportRepository.reports.sortedByDescending { it.isOpen }
    val filtered = source.filter {
        when (filter) {
            ReportFilter.ALL -> true
            ReportFilter.ACTIVE -> it.isOpen
            ReportFilter.RESOLVED -> !it.isOpen
        }
    }.filter {
        query.isBlank() || it.title.contains(query, true) || it.id.contains(query, true) ||
                it.building.contains(query, true) || it.room.contains(query, true) || it.category.contains(query, true)
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
        ScreenHeader("Reports", if (tab == 0) "Track problems you've reported or follow" else "Everything reported on campus")
        Spacer(Modifier.height(10.dp))
        TabRow(selectedTabIndex = tab, containerColor = Color.Transparent, contentColor = CfGreen) {
            listOf("My reports", "Campus").forEachIndexed { i, label ->
                Tab(selected = tab == i, onClick = { tab = i },
                    text = { Text(label, fontWeight = FontWeight.SemiBold) },
                    selectedContentColor = CfGreen, unselectedContentColor = CfMuted)
            }
        }
        Spacer(Modifier.height(14.dp))
        CfTextField(query, { query = it }, "Search reports", trailingIcon = { Icon(Icons.Default.Search, null) })
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportFilter.entries.forEach { f ->
                FilterChip(
                    selected = filter == f,
                    onClick = { filterIndex = f.ordinal },
                    label = { Text(f.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CfMint,
                        selectedLabelColor = CfGreen
                    )
                )
            }
        }
        if (tab == 1) {
            Text("Check here before reporting. If your problem is listed, open it and press \"I'm affected too\".",
                fontSize = 12.sp, color = CfMuted, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(14.dp))
        if (filtered.isEmpty()) {
            Text("No reports found.", color = CfMuted, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtered, key = { it.id }) { ReportCard(it, showId = true) { onOpenReport(it.id) } }
            }
        }
    }
}