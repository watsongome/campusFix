package com.example.campusfix.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusfix.ui.components.*
import com.example.campusfix.ui.theme.*

@Composable
fun LoginScreen(onSignIn: (String) -> Unit = {}, onCreateAccount: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Spacer(Modifier.height(48.dp))
        Text("CampusFix", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CfGreen)
        Text("University maintenance reporting", fontSize = 13.sp, color = CfMuted)
        Spacer(Modifier.height(32.dp))

        CfTextField(
            email, { email = it }, "Email address",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))
        CfTextField(
            password, { password = it }, "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show", color = CfGreen, fontSize = 12.sp)
                }
            }
        )
        TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
            Text("Forgot password?", color = CfGreen, fontSize = 12.sp)
        }
        Spacer(Modifier.height(8.dp))
        PrimaryButton("Sign in", onClick = { onSignIn(email) })
        Spacer(Modifier.height(16.dp))
        Text("or", color = CfMuted, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        SecondaryButton("Create an account", onClick = onCreateAccount)
        Spacer(Modifier.height(40.dp))
        Text(
            "Report campus problems clearly.", fontWeight = FontWeight.Bold, color = CfText,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        Text(
            "Track each report from submission to resolution.", fontSize = 12.sp, color = CfMuted,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
    }
}
