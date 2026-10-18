package com.example.campusfix.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.ui.auth.LoginScreen
import com.example.campusfix.ui.auth.SignUpScreen
import com.example.campusfix.ui.home.HomeScreen
import com.example.campusfix.ui.profile.ProfileScreen
import com.example.campusfix.ui.report.*
import com.example.campusfix.ui.theme.*

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val REPORTS = "reports"
    const val PROFILE = "profile"
    const val NEW = "report/new"
    const val SUBMITTED = "submitted/{id}"
    const val DETAILS = "details/{id}"
}

private fun NavHostController.goTab(route: String) = navigate(route) {
    popUpTo(Routes.HOME) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

@Composable
fun CampusFixApp() {
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    val tabs = listOf(
        Triple(Routes.HOME, "Home", Icons.Default.Home),
        Triple(Routes.REPORTS, "My Reports", Icons.AutoMirrored.Filled.List),
        Triple(Routes.PROFILE, "Profile", Icons.Default.Person)
    )

    Scaffold(
        containerColor = CfBackground,
        bottomBar = {
            if (route in tabs.map { it.first }) {
                NavigationBar(containerColor = Color.White) {
                    tabs.forEach { (r, label, icon) ->
                        NavigationBarItem(
                            selected = route == r,
                            onClick = { nav.goTab(r) },
                            icon = { Icon(icon, label) },
                            label = { Text(label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CfGreen, selectedTextColor = CfGreen, indicatorColor = CfMint
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = Routes.LOGIN, modifier = Modifier.padding(padding)) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onSignIn = { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                    onCreateAccount = { nav.navigate(Routes.SIGNUP) }
                )
            }
            composable(Routes.SIGNUP) {
                SignUpScreen(
                    onBack = { nav.popBackStack() },
                    onCreated = { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    onReportProblem = { nav.navigate(Routes.NEW) },
                    onViewAll = { nav.goTab(Routes.REPORTS) },
                    onOpenReport = { nav.navigate("details/$it") }
                )
            }
            composable(Routes.REPORTS) { MyReportsScreen(onOpenReport = { nav.navigate("details/$it") }) }
            composable(Routes.PROFILE) {
                ProfileScreen(onLogout = {
                    nav.navigate(Routes.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
                })
            }
            composable(Routes.NEW) {
                ReportProblemScreen(
                    onBack = { nav.popBackStack() },
                    onSubmit = { title, category, location, description, hasPhoto ->
                        val report = MockReportRepository.add(title, category, location, description, hasPhoto)
                        nav.navigate("submitted/${report.id}") { popUpTo(Routes.NEW) { inclusive = true } }
                    }
                )
            }
            composable(Routes.SUBMITTED) { entry ->
                SubmittedScreen(
                    reportId = entry.arguments?.getString("id").orEmpty(),
                    onViewReports = { nav.goTab(Routes.REPORTS) }
                )
            }
            composable(Routes.DETAILS) { entry ->
                ReportDetailsScreen(
                    reportId = entry.arguments?.getString("id").orEmpty(),
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
