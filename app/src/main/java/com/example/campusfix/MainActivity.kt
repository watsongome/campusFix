package com.example.campusfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campusfix.navigation.CampusFixApp
import com.example.campusfix.ui.auth.LoginScreen
import com.example.campusfix.ui.auth.SignUpScreen
import com.example.campusfix.ui.home.HomeScreen
import com.example.campusfix.ui.report.MyReportsScreen
import com.example.campusfix.ui.report.ReportDetailsScreen
import com.example.campusfix.ui.report.ReportProblemScreen
import com.example.campusfix.ui.report.SubmittedScreen
import com.example.campusfix.ui.theme.CampusFixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusFixTheme {
                CampusFixApp()
            }
        }
    }
}

// PREVIEWS
// Whole app.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    CampusFixTheme {
        CampusFixApp()
    }
}

// Individual screens
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() = CampusFixTheme {
    LoginScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreview() = CampusFixTheme {
    SignUpScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() = CampusFixTheme {
    HomeScreen(onReportProblem = {}, onViewAll = {}, onOpenReport = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportProblemPreview() = CampusFixTheme {
    ReportProblemScreen(onBack = {}, onSubmit = { _, _, _, _, _ -> })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubmittedPreview() = CampusFixTheme {
    SubmittedScreen(reportId = "CF-2026-0043", onViewReports = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyReportsPreview() = CampusFixTheme {
    MyReportsScreen(onOpenReport = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportDetailsPreview() = CampusFixTheme {
    ReportDetailsScreen(reportId = "CF-2026-0042", onBack = {})
}