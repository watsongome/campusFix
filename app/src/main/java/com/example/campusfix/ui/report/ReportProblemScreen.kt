package com.example.campusfix.ui.report

import android.content.ActivityNotFoundException
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.campusBuildings
import com.example.campusfix.data.model.reportCategories
import com.example.campusfix.ui.components.*
import com.example.campusfix.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit,
    onOpenReport: (String) -> Unit,
    onSubmit: (title: String, category: String, building: String, room: String, description: String, hasPhoto: Boolean) -> Unit
) {
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var building by rememberSaveable { mutableStateOf("") }
    var room by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var pendingCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }   // survives rotation while the camera is open
    var showDuplicateDialog by remember { mutableStateOf(false) }

    // Photo from the gallery. Cancelling must not wipe a photo that is already attached.
    val galleryPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) photoUri = uri
    }
    // Photo from the camera. The system camera app saves into pendingCameraUri; no CAMERA permission needed.
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) photoUri = pendingCameraUri
        pendingCameraUri = null
    }

    fun takePhoto() {
        val uri = createImageUri(context)
        pendingCameraUri = uri
        try {
            camera.launch(uri)
        } catch (e: ActivityNotFoundException) {
            pendingCameraUri = null
            Toast.makeText(context, "No camera app found on this device", Toast.LENGTH_SHORT).show()
        }
    }

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

        PhotoSection(
            photoUri = photoUri,
            onTakePhoto = ::takePhoto,
            onChooseFromGallery = {
                galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onRemove = { photoUri = null }
        )

        Spacer(Modifier.height(24.dp))
        PrimaryButton("Submit report", enabled = valid) {
            if (similar.isNotEmpty()) showDuplicateDialog = true else submit()
        }
        Text("You'll receive a unique report ID.", fontSize = 11.sp, color = CfMuted, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
    }
}

@Composable
private fun PhotoSection(
    photoUri: Uri?,
    onTakePhoto: () -> Unit,
    onChooseFromGallery: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val thumbnail by produceState<ImageBitmap?>(initialValue = null, photoUri) {
        value = photoUri?.let { withContext(Dispatchers.IO) { decodeThumbnail(context, it) } }
    }

    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CfSurface).padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(56.dp).clip(RoundedCornerShape(10.dp)).background(CfMint),
                contentAlignment = Alignment.Center
            ) {
                val bitmap = thumbnail
                if (bitmap != null) {
                    Image(bitmap, "Attached photo", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Icon(if (photoUri == null) Icons.Default.Add else Icons.Default.Check, null, tint = CfGreen)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(if (photoUri == null) "Add a photo" else "Photo attached", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CfText)
                Text(if (photoUri == null) "Optional" else "Take or choose another to replace it", fontSize = 11.sp, color = CfMuted)
            }
            if (photoUri != null) TextButton(onClick = onRemove) { Text("Remove", color = CfGreen) }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PhotoButton("Take photo", Modifier.weight(1f), onTakePhoto)
            PhotoButton("Choose from gallery", Modifier.weight(1f), onChooseFromGallery)
        }
    }
}

@Composable
private fun PhotoButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick, modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CfBorder),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CfGreen)
    ) { Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
}
