package com.example.campusfix

//import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.Session
import com.example.campusfix.navigation.CampusFixApp
import com.example.campusfix.ui.theme.CampusFixTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// UI tests
@RunWith(AndroidJUnit4::class)
class AppFlowTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        Session.signOut()
        MockReportRepository.reset()
        rule.setContent { CampusFixTheme { CampusFixApp() } }
    }

    private fun signInAs(email: String) {
        rule.onNodeWithText("Email address").performTextInput(email)
        rule.onNodeWithText("Sign in").performClick()
    }

    private fun openReportForm() {
        signInAs("student@unima.ac.mw")
        rule.onNodeWithText("Something on campus isn't working properly?").performClick()
    }

    // Student

    @Test
    fun loginScreen_isShownFirst() {
        rule.onNodeWithText("CampusFix").assertExists()
        rule.onNodeWithText("Create an account").assertExists()
    }

    @Test
    fun studentSignIn_showsHome() {
        signInAs("student@unima.ac.mw")
        rule.onNodeWithText("What needs fixing?").assertExists()
        rule.onNodeWithText("How it works").assertExists()
    }

    @Test
    fun reportForm_submitIsDisabledUntilFilled() {
        openReportForm()
        rule.onNodeWithText("Submit report").assertIsNotEnabled()
    }

    @Test
    fun reportForm_warnsAboutPossibleDuplicate() {
        openReportForm()

        rule.onNodeWithText("Category").performClick()
        rule.onNodeWithText("Electrical").performClick()
        rule.onNodeWithText("Building").performClick()
        rule.onNodeWithText("Library").performClick()

        rule.onNodeWithText("Already reported? 1 open report matches").assertExists()
    }

    @Test
    fun reportForm_offersCameraAndGalleryForPhotos() {
        openReportForm()
        rule.onNodeWithText("Take photo").performScrollTo().assertExists()
        rule.onNodeWithText("Choose from gallery").performScrollTo().assertExists()
    }

    @Test
    fun reportForm_noWarningWhenNothingMatches() {
        openReportForm()

        rule.onNodeWithText("Category").performClick()
        rule.onNodeWithText("Furniture").performClick()
        rule.onNodeWithText("Building").performClick()
        rule.onNodeWithText("Library").performClick()

        rule.onNodeWithText("No open reports for Furniture in Library. You're good to continue.").assertExists()
    }

    @Test
    fun student_doesNotSeeOfficerControls() {
        signInAs("student@unima.ac.mw")
        rule.onNodeWithText("Leaking tap").performScrollTo().performClick()

        rule.onNodeWithText("Report details").assertExists()
        rule.onNodeWithText("Mark as Assigned").assertDoesNotExist()
    }

    // Officer

    @Test
    fun officerSignIn_showsTheQueueInsteadOfHome() {
        signInAs("officer@unima.ac.mw")
        rule.onNodeWithText("Maintenance queue").assertExists()
    }

    @Test
    fun officerQueue_canBeFilteredByStatus() {
        signInAs("officer@unima.ac.mw")

        rule.onNodeWithTag("filter_RESOLVED").performClick()

        rule.onNodeWithText("Damaged desk").assertExists()
        rule.onNodeWithText("Leaking tap").assertDoesNotExist()
    }

    @Test
    fun officerCanAdvanceAReportToTheNextStatus() {
        signInAs("officer@unima.ac.mw")
        rule.onNodeWithTag("filter_REPORTED").performClick()
        rule.onNodeWithText("Leaking tap").performClick()

        rule.onNodeWithText("Mark as Assigned").performClick()

        rule.onNodeWithText("Mark as In Progress").assertExists()
    }
}
