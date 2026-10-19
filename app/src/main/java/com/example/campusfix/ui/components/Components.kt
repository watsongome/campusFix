package com.example.campusfix.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.data.MockReportRepository
import com.example.campusfix.data.model.Report
import com.example.campusfix.data.model.ReportStatus
import com.example.campusfix.ui.theme.*

@Composable
fun StatusChip(status: ReportStatus) {
    val (bg, fg) = when (status) {
        ReportStatus.REPORTED -> CfAmberBg to CfAmber
        ReportStatus.ASSIGNED -> CfBlueBg to CfBlue
        ReportStatus.IN_PROGRESS -> CfMint to CfGreen
        ReportStatus.RESOLVED -> CfResolvedBg to CfResolved
    }
    Text(
        text = status.label, color = fg, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun ReportCard(report: Report, showId: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    val note = buildList {
        if (report.affectedCount > 1) add("${report.affectedCount} affected")
        if (MockReportRepository.isFollowing(report)) add("Following")
    }.joinToString(" \u2022 ")

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CfSurface),
        border = BorderStroke(1.dp, CfBorder),
        modifier = Modifier.fillMaxWidth().clip(shape).clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            val warn = report.status == ReportStatus.REPORTED
            Box(
                Modifier.size(40.dp).background(if (warn) CfAmberBg else CfMint, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Warning, null, Modifier.size(20.dp), tint = if (warn) CfAmber else CfGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(report.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CfText)
                Text(report.location, fontSize = 12.sp, color = CfMuted)
                if (showId) Text(report.id, fontSize = 11.sp, color = CfMuted)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(report.status)
                    if (note.isNotEmpty()) Text(note, fontSize = 11.sp, color = CfMuted, modifier = Modifier.padding(start = 8.dp))
                }
            }
            Icon(Icons.Default.KeyboardArrowRight, null, tint = CfMuted)
        }
    }
}

@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick, enabled = enabled,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CfGreen, contentColor = CfOnGreen)
    ) { Text(text, fontWeight = FontWeight.SemiBold) }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CfBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = CfSurface, contentColor = CfGreen)
    ) { Text(text, fontWeight = FontWeight.SemiBold) }
}

@Composable
fun CfTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine, minLines = minLines, readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(12.dp),
        // Text colors come from the theme, so typed text stays readable in light AND dark mode.
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = CfGreen,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = CfGreen,
            unfocusedLabelColor = CfMuted,
            cursorColor = CfGreen,
            focusedContainerColor = CfSurface,
            unfocusedContainerColor = CfSurface
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun CfDropdownField(
    value: String,
    label: String,
    placeholder: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    Box {
        CfTextField(
            value, {}, label, readOnly = true, placeholder = placeholder,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
        )
        Box(Modifier.matchParentSize().clickable { open = true })
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onSelect(option); open = false })
            }
        }
    }
}

@Composable
fun ScreenHeader(title: String, subtitle: String? = null) {
    Column {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CfText)
        if (subtitle != null) Text(subtitle, fontSize = 13.sp, color = CfMuted)
    }
}
