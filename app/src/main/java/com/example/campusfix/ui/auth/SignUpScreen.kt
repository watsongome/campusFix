package com.example.campusfix.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.ui.components.*
import com.example.campusfix.ui.theme.*

@Composable
fun SignUpScreen(onBack: () -> Unit = {}, onCreated: (String, String) -> Unit = { _, _ -> }) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val mismatch = confirm.isNotEmpty() && confirm != password
    val valid = name.isNotBlank() && email.isNotBlank() && password.length >= 6 && password == confirm

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        Spacer(Modifier.height(8.dp))
        ScreenHeader("Create an account", "Use your university details to get started.")
        Spacer(Modifier.height(24.dp))
        CfTextField(name, { name = it }, "Full name")
        Spacer(Modifier.height(12.dp))
        CfTextField(
            email, { email = it }, "Email address",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))
        CfTextField(
            password, { password = it }, "Password (min. 6 characters)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(12.dp))
        CfTextField(
            confirm, { confirm = it }, "Confirm password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        if (mismatch) Text("Passwords do not match", color = CfAmber, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Create account", enabled = valid, onClick = { onCreated(name, email) })
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", fontSize = 13.sp, color = CfMuted)
            TextButton(onClick = onBack) { Text("Sign in", color = CfGreen) }
        }
    }
}
