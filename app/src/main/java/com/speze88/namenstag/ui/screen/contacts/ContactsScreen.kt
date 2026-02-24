package com.speze88.namenstag.ui.screen.contacts

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.MatchType
import com.speze88.namenstag.ui.component.ContactAvatar
import com.speze88.namenstag.ui.component.PermissionDialog
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onSaintClick: (Long) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            viewModel.onPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    if (uiState.showPermissionRationale) {
        PermissionDialog(
            title = "Kontakte-Zugriff",
            message = "Die App benötigt Zugriff auf deine Kontakte, um Namenstage deiner Kontakte zu finden.",
            onConfirm = {
                viewModel.dismissRationale()
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            },
            onDismiss = { viewModel.dismissRationale() },
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kontakte") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )

        when {
            !uiState.permissionGranted && !uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Kontakte-Berechtigung erforderlich",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        TextButton(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            },
                        ) {
                            Text("Berechtigung erteilen")
                        }
                    }
                }
            }

            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.contactNameDays.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Keine Kontakte mit Namenstagen gefunden.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        uiState.contactNameDays,
                        key = { it.contact.id },
                    ) { contactNameDay ->
                        ContactNameDayCard(
                            contactNameDay = contactNameDay,
                            onSaintClick = onSaintClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactNameDayCard(
    contactNameDay: ContactNameDay,
    onSaintClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            ContactAvatar(name = contactNameDay.contact.displayName)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contactNameDay.contact.displayName,
                    style = MaterialTheme.typography.titleMedium,
                )
                val matchLabel = when (contactNameDay.matchType) {
                    MatchType.EXACT -> ""
                    MatchType.ALIAS -> " (Alias: ${contactNameDay.contact.givenName})"
                    MatchType.PHONETIC -> " (Phonetisch)"
                }
                contactNameDay.nameDays.forEach { nameDay ->
                    Text(
                        text = "${nameDay.name}$matchLabel – ${nameDay.day}.${nameDay.month}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onSaintClick(nameDay.saint.id) },
                    )
                }
            }
        }
    }
}
