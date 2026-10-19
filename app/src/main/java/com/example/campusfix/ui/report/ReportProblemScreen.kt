package com.example.campusfix.ui.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.campusBuildings
import com.example.campusfix.data.model.reportCategories
import com.example.campusfix.ui.components.*
import com.example.campusfix.ui.theme.*

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit,
    onOpenReport: (String) -> Unit,
    onSubmit: (title: String, category: String, building: String, room: String, description: String, hasPhoto: Boolean) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var building by rememberSaveable { mutableStateOf("") }
    var room by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showDuplicateDialog by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { photoUri = it }
    val valid = title.isNotBlank() && category.isNotBlank() && building.isNotBlank() && description.isNotBlank()
    val similar = if (building.isNotBlank() && category.isNotBlank())
        MockReportRepository.findSimilar(building, category) else emptyList()

    fun submit() = onSubmit(title, category, building, room, description, photoUri != null)

    if (showDuplicateDialog) {
        AlertDialog(
            onDismissRequest = { showDuplicateDialog = false },
            title = { Text("This may already be reported") },
            text = {
                Text("There ${if (similar.size == 1) "is 1 open report" else "are ${similar.size} open reports"} for $category in $building. " +
                    "Please check ${if (similar.size == 1) "it" else "them"} first so maintenance isn't flooded with duplicates.")
            },
            confirmButton = { TextButton(onClick = { showDuplicateDialog = false; submit() }) { Text("Submit anyway", color = CfGreen) } },
            dismissButton = { TextButton(onClick = { showDuplicateDialog = false }) { Text("Review matches", color = CfGreen) } }
        )
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            Text("Report a problem", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CfText)
        }
        Text("Give enough detail for the maintenance team to act.", fontSize = 12.sp, color = CfMuted,
            modifier = Modifier.padding(start = 12.dp, bottom = 16.dp))

        CfDropdownField(category, "Category", "Select category", reportCategories) { category = it }
        Spacer(Modifier.height(12.dp))
        CfDropdownField(building, "Building", "Where is it?", campusBuildings) { building = it }

        // Duplicate check: appears as soon as category + building are chosen
        if (category.isNotBlank() && building.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            if (similar.isEmpty()) {
                Text("No open reports for $category in $building. You're good to continue.", fontSize = 12.sp, color = CfGreen)
            } else {
                Column(
                    Modifier.fillMaxWidth().background(CfAmberBg, RoundedCornerShape(14.dp)).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Already reported? ${similar.size} open ${if (similar.size == 1) "report matches" else "reports match"}",
                        fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CfAmber)
                    Text("Tap one to check it. If it's your problem, press \"I'm affected too\" there instead of reporting again.",
                        fontSize = 12.sp, color = CfAmber)
                    similar.forEach { ReportCard(it, showId = true) { onOpenReport(it.id) } }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        CfTextField(title, { title = it }, "Title", placeholder = "e.g. Broken projector")
        Spacer(Modifier.height(12.dp))
        CfTextField(room, { room = it }, "Room / spot (optional)", placeholder = "e.g. Room 2, Computer Lab 2")
        Spacer(Modifier.height(12.dp))
        CfTextField(description, { description = it }, "Description", placeholder = "Describe what is wrong...", singleLine = false, minLines = 4)
        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CfSurface)
                .clickable { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(40.dp).background(CfMint, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(if (photoUri == null) Icons.Default.Add else Icons.Default.Check, null, tint = CfGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(if (photoUri == null) "Add a photo" else "Photo attached", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CfText)
                Text(if (photoUri == null) "Optional" else "Tap to change", fontSize = 11.sp, color = CfMuted)
            }
        }

        Spacer(Modifier.height(24.dp))
        PrimaryButton("Submit report", enabled = valid) {
            if (similar.isNotEmpty()) showDuplicateDialog = true else submit()
        }
        Text("You'll receive a unique report ID.", fontSize = 11.sp, color = CfMuted, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
    }
}
