package com.example.campusfix.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.Session
import com.example.campusfix.data.model.UserRole
import com.example.campusfix.ui.auth.LoginScreen
import com.example.campusfix.ui.auth.SignUpScreen
import com.example.campusfix.ui.home.HomeScreen
import com.example.campusfix.ui.officer.OfficerHomeScreen
import com.example.campusfix.ui.profile.ProfileScreen
import com.example.campusfix.ui.report.*
import com.example.campusfix.ui.theme.*

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val REPORTS = "reports"
    const val CAMPUS = "campus"
    const val PROFILE = "profile"
    const val OFFICER_HOME = "officer_home"
    const val NEW = "report/new"
    const val SUBMITTED = "submitted/{id}"
    const val DETAILS = "details/{id}"

    const val REPORTS_ACTIVE = "reports/active"

    const val REPORTS_RESOLVED = "reports/resolved"
}

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private fun NavHostController.goTab(route: String, home: String) = navigate(route) {
    popUpTo(home)
    launchSingleTop = true
}

private val reportRoutes = listOf(
    Routes.REPORTS, Routes.CAMPUS, Routes.REPORTS_ACTIVE, Routes.REPORTS_RESOLVED
)

@Composable
fun CampusFixApp() {
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    val role = Session.user?.role

    // The role decides which home and which tabs exist. Add a role = add a branch here.
    val home = if (role == UserRole.OFFICER) Routes.OFFICER_HOME else Routes.HOME
    val tabs = when (role) {
        UserRole.OFFICER -> listOf(
            TabItem(Routes.OFFICER_HOME, "Queue", Icons.AutoMirrored.Filled.List),
            TabItem(Routes.PROFILE, "Profile", Icons.Default.Person)
        )
        else -> listOf(
            TabItem(Routes.HOME, "Home", Icons.Default.Home),
            TabItem(Routes.REPORTS, "Reports", Icons.AutoMirrored.Filled.List),
            TabItem(Routes.PROFILE, "Profile", Icons.Default.Person)
        )
    }
    val showBar = role != null && (route in tabs.map { it.route } || route in reportRoutes)

    Scaffold(
        containerColor = CfBackground,
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = CfSurface) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route || (tab.route == Routes.REPORTS && route in reportRoutes),
                            onClick = { nav.goTab(tab.route, home) },
                            icon = { Icon(tab.icon, tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CfGreen, selectedTextColor = CfGreen,
                                indicatorColor = CfMint, unselectedIconColor = CfMuted, unselectedTextColor = CfMuted
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
                    onSignIn = { email ->
                        Session.signIn(email)
                        val dest = if (Session.user?.role == UserRole.OFFICER) Routes.OFFICER_HOME else Routes.HOME
                        nav.navigate(dest) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    },
                    onCreateAccount = { nav.navigate(Routes.SIGNUP) }
                )
            }
            composable(Routes.SIGNUP) {
                SignUpScreen(
                    onBack = { nav.popBackStack() },
                    onCreated = { name, email ->
                        Session.signUp(name, email)
                        nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    }
                )
            }

            // ----- Regular user -----
            composable(Routes.HOME) {
                HomeScreen(
                    onReportProblem = { nav.navigate(Routes.NEW) },
                    onViewAll = { nav.goTab(Routes.REPORTS, home) },
                    onBrowseCampus = { nav.navigate(Routes.CAMPUS) { launchSingleTop = true } },
                    onOpenReport = { nav.navigate("details/$it") },
                    onActiveClick = { nav.navigate(Routes.REPORTS_ACTIVE) { launchSingleTop = true } },
                    onResolvedClick = { nav.navigate(Routes.REPORTS_RESOLVED) { launchSingleTop = true } }
                )
            }

            composable(Routes.REPORTS_ACTIVE) {
                MyReportsScreen(initialTab = 0, initialFilter = ReportFilter.ACTIVE, onOpenReport = { nav.navigate("details/$it") })
            }

            composable(Routes.REPORTS_RESOLVED) {
                MyReportsScreen(initialTab = 0, initialFilter = ReportFilter.RESOLVED, onOpenReport = { nav.navigate("details/$it") })
            }
            composable(Routes.REPORTS) { MyReportsScreen(initialTab = 0, onOpenReport = { nav.navigate("details/$it") }) }
            composable(Routes.CAMPUS) { MyReportsScreen(initialTab = 1, onOpenReport = { nav.navigate("details/$it") }) }
            composable(Routes.NEW) {
                ReportProblemScreen(
                    onBack = { nav.popBackStack() },
                    onOpenReport = { nav.navigate("details/$it") },
                    onSubmit = { title, category, building, room, description, hasPhoto ->
                        val report = MockReportRepository.add(title, category, building, room, description, hasPhoto)
                        nav.navigate("submitted/${report.id}") { popUpTo(Routes.NEW) { inclusive = true } }
                    }
                )
            }
            composable(Routes.SUBMITTED) { entry ->
                SubmittedScreen(
                    reportId = entry.arguments?.getString("id").orEmpty(),
                    onViewReports = { nav.goTab(Routes.REPORTS, home) }
                )
            }

            // ----- Officer -----
            composable(Routes.OFFICER_HOME) { OfficerHomeScreen(onOpenReport = { nav.navigate("details/$it") }) }

            // ----- Shared -----
            composable(Routes.DETAILS) { entry ->
                ReportDetailsScreen(
                    reportId = entry.arguments?.getString("id").orEmpty(),
                    onBack = { nav.popBackStack() }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onLogout = {
                    Session.signOut()
                    nav.navigate(Routes.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
                })
            }
        }
    }
}
