package com.example.campusfix

import android.content.res.Configuration
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

// ---------- PREVIEWS (key screens have a light and a dark version) ----------
private const val DARK = Configuration.UI_MODE_NIGHT_YES

@Preview(showBackground = true, showSystemUi = true)
@Composable fun AppPreview() = CampusFixTheme { CampusFixApp() }

@Preview(showBackground = true, showSystemUi = true, uiMode = DARK)
@Composable fun AppPreviewDark() = CampusFixTheme { CampusFixApp() }

@Preview(showBackground = true, name = "Login")
@Composable fun LoginPreview() = CampusFixTheme { LoginScreen() }

@Preview(showBackground = true, name = "Login dark", uiMode = DARK)
@Composable fun LoginPreviewDark() = CampusFixTheme { LoginScreen() }

@Preview(showBackground = true, name = "Sign up")
@Composable fun SignUpPreview() = CampusFixTheme { SignUpScreen() }

@Preview(showBackground = true, name = "Home", heightDp = 1100)
@Composable fun HomePreview() = CampusFixTheme {
    HomeScreen(onReportProblem = {}, onViewAll = {}, onBrowseCampus = {}, onOpenReport = {})
}

@Preview(showBackground = true, name = "Home dark", heightDp = 1100, uiMode = DARK)
@Composable fun HomePreviewDark() = CampusFixTheme {
    HomeScreen(onReportProblem = {}, onViewAll = {}, onBrowseCampus = {}, onOpenReport = {})
}

@Preview(showBackground = true, name = "Report a problem", heightDp = 900)
@Composable fun ReportProblemPreview() = CampusFixTheme {
    ReportProblemScreen(onBack = {}, onOpenReport = {}, onSubmit = { _, _, _, _, _, _ -> })
}

@Preview(showBackground = true, name = "Report a problem dark", heightDp = 900, uiMode = DARK)
@Composable fun ReportProblemPreviewDark() = CampusFixTheme {
    ReportProblemScreen(onBack = {}, onOpenReport = {}, onSubmit = { _, _, _, _, _, _ -> })
}

@Preview(showBackground = true, name = "Submitted")
@Composable fun SubmittedPreview() = CampusFixTheme {
    SubmittedScreen(reportId = "CF-2026-0045", onViewReports = {})
}

@Preview(showBackground = true, name = "Reports")
@Composable fun MyReportsPreview() = CampusFixTheme { MyReportsScreen(onOpenReport = {}) }

@Preview(showBackground = true, name = "Report details", heightDp = 900)
@Composable fun ReportDetailsPreview() = CampusFixTheme {
    ReportDetailsScreen(reportId = "CF-2026-0044", onBack = {})
}
