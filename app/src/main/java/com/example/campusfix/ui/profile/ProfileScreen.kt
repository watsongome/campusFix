package com.example.campusfix.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.Session
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.components.SecondaryButton
import com.example.campusfix.ui.theme.*

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val user = Session.user
    val name = user?.name ?: "Demo Student"
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.fillMaxWidth()) { ScreenHeader("Profile") }
        Spacer(Modifier.height(32.dp))
        Box(Modifier.size(88.dp).background(CfGreen, CircleShape), contentAlignment = Alignment.Center) {
            Text(name.take(1).uppercase(), color = CfOnGreen, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CfText)
        Text(user?.email ?: "student@unima.ac.mw", fontSize = 13.sp, color = CfMuted)
        Spacer(Modifier.height(8.dp))
        Text("Role: ${user?.role?.label ?: "Student / Staff"}", fontSize = 12.sp, color = CfGreen, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.weight(1f))
        SecondaryButton("Log out", onClick = onLogout)
    }
}
