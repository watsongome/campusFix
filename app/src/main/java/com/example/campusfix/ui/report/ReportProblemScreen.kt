package com.example.campusfix.ui.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.model.reportCategories
import com.example.campusfix.ui.components.*
import com.example.campusfix.ui.theme.*

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit,
    onSubmit: (title: String, category: String, location: String, description: String, hasPhoto: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var menuOpen by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { photoUri = it }
    val valid = title.isNotBlank() && category.isNotBlank() && description.isNotBlank() && location.isNotBlank()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            Text("Report a problem", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CfText)
        }
        Text("Give enough detail for the maintenance team to act.", fontSize = 12.sp, color = CfMuted, modifier = Modifier.padding(start = 12.dp, bottom = 16.dp))

        CfTextField(title, { title = it }, "Title", placeholder = "e.g. Broken projector")
        Spacer(Modifier.height(12.dp))

        Box {
            CfTextField(
                category, {}, "Category", readOnly = true, placeholder = "Select category",
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
            )
            Box(Modifier.matchParentSize().clickable { menuOpen = true })
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                reportCategories.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { category = it; menuOpen = false })
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        CfTextField(description, { description = it }, "Description", placeholder = "Describe what is wrong...", singleLine = false, minLines = 4)
        Spacer(Modifier.height(12.dp))
        CfTextField(location, { location = it }, "Location", placeholder = "e.g. Computer Lab 2")
        Spacer(Modifier.height(12.dp))

        val shape = RoundedCornerShape(12.dp)
        Row(
            Modifier.fillMaxWidth().clip(shape).background(Color.White)
                .clickable { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(40.dp).background(CfMint, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(if (photoUri == null) Icons.Default.Add else Icons.Default.Check, null, tint = CfGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(if (photoUri == null) "Add a photo" else "Photo attached", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(if (photoUri == null) "Optional" else "Tap to change", fontSize = 11.sp, color = CfMuted)
            }
        }

        Spacer(Modifier.height(24.dp))
        PrimaryButton("Submit report", enabled = valid) {
            onSubmit(title, category, location, description, photoUri != null)
        }
        Text("You'll receive a unique report ID.", fontSize = 11.sp, color = CfMuted, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportProblemPreview() = CampusFixTheme {
    ReportProblemScreen(onBack = {}, onSubmit = { _, _, _, _, _ -> })
}