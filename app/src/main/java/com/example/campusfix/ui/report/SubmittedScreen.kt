package com.example.campusfix.ui.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.ui.components.PrimaryButton
import com.example.campusfix.ui.components.StatusChip
import com.example.campusfix.ui.theme.*

@Composable
fun SubmittedScreen(reportId: String, onViewReports: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Report submitted", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CfText)
        Spacer(Modifier.height(16.dp))

        Column(
            Modifier.fillMaxWidth().background(CfMint, RoundedCornerShape(20.dp)).padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.size(72.dp).background(CfSurface, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, null, tint = CfGreen, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(14.dp))
            Text("You're all set.", fontWeight = FontWeight.Bold, color = CfText)
            Text("Your report has been recorded.", fontSize = 12.sp, color = CfMuted)
        }

        Spacer(Modifier.height(20.dp))
        Text("REPORT ID", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = CfMuted)
        Text(reportId, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CfGreen)
        Spacer(Modifier.height(12.dp))

        Card(
            Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CfSurface),
            border = BorderStroke(1.dp, CfBorder)
        ) {
            Column(Modifier.padding(14.dp)) {
                Text("Current status", fontSize = 11.sp, color = CfMuted)
                Spacer(Modifier.height(6.dp))
                StatusChip(ReportStatus.REPORTED)
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("What happens next?", fontWeight = FontWeight.Bold, color = CfText)
        Text(
            "A maintenance officer will review the report and update its status as work progresses.",
            fontSize = 12.sp, color = CfMuted, modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton("View my reports", onClick = onViewReports)
    }
}
