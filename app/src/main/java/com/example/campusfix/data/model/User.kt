package com.example.campusfix.data.model

/** Add ADMIN (or others) here later; the rest of the app adapts through the role. */
enum class UserRole(val label: String) {
    USER("Student / Staff"),
    OFFICER("Maintenance officer")
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)
