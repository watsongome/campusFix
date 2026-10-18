package com.example.campusfix.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.ui.components.CfTextField
import com.example.campusfix.ui.components.ReportCard
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.theme.CampusFixTheme
import com.example.campusfix.ui.theme.CfMuted

@Composable
fun MyReportsScreen(onOpenReport: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = MockReportRepository.reports.filter {
        query.isBlank() || it.title.contains(query, true) || it.id.contains(query, true) || it.location.contains(query, true)
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
        ScreenHeader("My Reports", "Track problems you've reported")
        Spacer(Modifier.height(14.dp))
        CfTextField(query, { query = it }, "Search reports", trailingIcon = { Icon(Icons.Default.Search, null) })
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyReportsPreview() = CampusFixTheme {
    MyReportsScreen(onOpenReport = {})
}