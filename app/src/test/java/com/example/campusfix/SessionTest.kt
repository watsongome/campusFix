package com.example.campusfix.data

import com.example.campusfix.data.model.UserRole
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SessionTest {

    @After
    fun tearDown() = Session.signOut()

    @Test
    fun signIn_regularEmail_isUser() {
        Session.signIn("student@unima.ac.mw")
        assertEquals(UserRole.USER, Session.user?.role)
    }

    @Test
    fun signIn_officerEmail_isOfficer() {
        Session.signIn("officer@unima.ac.mw")
        assertEquals(UserRole.OFFICER, Session.user?.role)
    }

    @Test
    fun signIn_officerPrefix_isNotCaseSensitive() {
        Session.signIn("Officer.Gome@unima.ac.mw")
        assertEquals(UserRole.OFFICER, Session.user?.role)
    }

    @Test
    fun signIn_blankEmail_fallsBackToDemoStudent() {
        Session.signIn("   ")
        assertEquals(UserRole.USER, Session.user?.role)
        assertEquals("student@unima.ac.mw", Session.user?.email)
    }

    @Test
    fun signIn_buildsDisplayNameFromEmail() {
        Session.signIn("gome@unima.ac.mw")
        assertEquals("Gome", Session.user?.name)
    }

    // Security rule: nobody can create an officer account by registering.
    @Test
    fun signUp_isAlwaysRegularUser_evenWithOfficerEmail() {
        Session.signUp("Sneaky", "officer@unima.ac.mw")
        assertEquals(UserRole.USER, Session.user?.role)
    }

    @Test
    fun signOut_clearsTheUser() {
        Session.signIn("student@unima.ac.mw")
        assertNotNull(Session.user)
        Session.signOut()
        assertNull(Session.user)
    }
}
