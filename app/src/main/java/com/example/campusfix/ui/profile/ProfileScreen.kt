package com.example.campusfix.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.ui.components.ScreenHeader
import com.example.campusfix.ui.components.SecondaryButton
import com.example.campusfix.ui.theme.*

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.fillMaxWidth()) { ScreenHeader("Profile") }
        Spacer(Modifier.height(32.dp))
        Box(Modifier.size(88.dp).background(CfGreen, CircleShape), contentAlignment = Alignment.Center) {
            Text("S", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Text("Demo Student", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CfText)
        Text("student@university.edu", fontSize = 13.sp, color = CfMuted)
        Spacer(Modifier.height(8.dp))
        Text("Role: Student", fontSize = 12.sp, color = CfGreen, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.weight(1f))
        SecondaryButton("Log out", onClick = onLogout)
    }
}
