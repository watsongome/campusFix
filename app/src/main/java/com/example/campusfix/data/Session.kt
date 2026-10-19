package com.example.campusfix.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.campusfix.data.model.User
import com.example.campusfix.data.model.UserRole

/**
 * Who is logged in. MOCK for now: an email starting with "officer" signs in as an officer.
 * With Supabase, the role comes from the user's profile row, never from the app or the user.
 */
object Session {
    var user by mutableStateOf<User?>(null)
        private set

    fun signIn(email: String) {
        val clean = email.trim().ifBlank { "student@unima.ac.mw" }
        val role = if (clean.startsWith("officer", ignoreCase = true)) UserRole.OFFICER else UserRole.USER
        val id = if (role == UserRole.OFFICER) "officer-1" else "me"
        user = User(id, displayName(clean), clean, role)
    }

    /** New accounts are ALWAYS regular users. Officers are promoted by an admin, never self-selected. */
    fun signUp(name: String, email: String) {
        user = User("me", name.trim().ifBlank { displayName(email) }, email.trim(), UserRole.USER)
    }

    fun signOut() { user = null }

    private fun displayName(email: String) =
        email.substringBefore("@").replaceFirstChar { it.uppercase() }
}
