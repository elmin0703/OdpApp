package com.example.odpapp.ui.theme.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.odpapp.MainActivity
import com.example.odpapp.data.auth.TokenStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiAuthScreen(navController: NavController) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val scope = rememberCoroutineScope()
    val uri = LocalUriHandler.current

    var tokenInput by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "API autentifikacija",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "1) Otvori stranicu i prijavi se kako bi dobio novi API token.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "https://odp.iddeea.gov.ba/user-zone/apiAuth",
                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    uri.openUri("https://odp.iddeea.gov.ba/user-zone/apiAuth")
                }
            )

            OutlinedTextField(
                value = tokenInput,
                onValueChange = { tokenInput = it },
                label = { Text("Unesi novi token (sa ili bez 'Bearer ')") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    scope.launch {
                        val normalized = normalizeBearer(tokenInput)
                        if (normalized.isBlank()) {
                            snackbarHostState.showSnackbar("Unesi token.")
                            return@launch
                        }

                        // 1) Snimi token
                        TokenStore.save(appContext, token = normalized)

                        // 2) (opciono) Kratka potvrda korisniku
                        snackbarHostState.showSnackbar("Token spremljen. Restartujem aplikaciju…")

                        // 3) HARD RESTART aplikacije
                        com.example.odpapp.util.AppRestarter.restartApp(context)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) { Text("Potvrdi") }

        }
    }
}

private fun normalizeBearer(raw: String): String {
    val t = raw.trim()
    if (t.isEmpty()) return ""
    return if (t.startsWith("Bearer ", ignoreCase = true)) t else "Bearer $t"
}
