package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.partsasign1.Viewmodels.AutentViewModel // <-- IMPORTACIÓN CORREGIDA
import androidx.navigation.compose.rememberNavController

@Composable
fun LogScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AutentViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Gestión Repuestos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.emailError != null,
            supportingText = { uiState.emailError?.let { Text(it) } }
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.passwordError != null,
            supportingText = { uiState.passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = viewModel::onLoginClick, modifier = Modifier.fillMaxWidth(),enabled = !uiState.isLoading) {
            Text("Ingresar")
        }
        LaunchedEffect(key1 = uiState.loginSuccess) {
            if (uiState.loginSuccess) {
                onLoginSuccess()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LogScreenPreview() {
    MaterialTheme {
        LogScreen(
            onLoginSuccess = { },
            viewModel = viewModel()
        )
    }
}

