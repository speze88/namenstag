package com.speze88.namenstag.ui.screen.today

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speze88.namenstag.domain.model.MatchType
import com.speze88.namenstag.domain.model.UpcomingContactNameDay
import com.speze88.namenstag.ui.component.ContactAvatar
import com.speze88.namenstag.ui.component.NameDayCard
import com.speze88.namenstag.ui.component.PermissionDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onSaintClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val hasContactsPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_CONTACTS,
    ) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            viewModel.onContactsPermissionGranted()
        } else {
            viewModel.onContactsPermissionDenied()
        }
    }

    LaunchedEffect(hasContactsPermission) {
        if (hasContactsPermission) {
            viewModel.onContactsPermissionGranted()
        }
    }

    if (uiState.showContactsPermissionRationale) {
        PermissionDialog(
            title = "Kontakte-Zugriff",
            message = "Die App benötigt Zugriff auf deine Kontakte, um den nächsten Namenstag deiner Kontakte zu zeigen.",
            onConfirm = {
                viewModel.dismissContactsPermissionRationale()
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            },
            onDismiss = { viewModel.dismissContactsPermissionRationale() },
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text("Heute")
                    Text(
                        text = LocalDate.now().format(
                            DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Suchen",
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (uiState.nameDays.isEmpty()) {
                        item {
                            Text(
                                text = "Heute ist kein Namenstag in der Datenbank.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = "Namenstage heute",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }
                        items(uiState.nameDays, key = { it.name }) { nameDay ->
                            NameDayCard(
                                nameDay = nameDay,
                                onClick = { onSaintClick(nameDay.saint.id) },
                            )
                        }
                    }

                    if (uiState.isLoadingUpcomingContacts) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (uiState.upcomingContactNameDays.isNotEmpty()) {
                        item {
                            Text(
                                text = "Als Nächstes bei deinen Kontakten",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            )
                        }
                        items(
                            uiState.upcomingContactNameDays,
                            key = { it.contactNameDay.contact.id },
                        ) { upcomingContactNameDay ->
                            UpcomingContactNameDayCard(
                                upcomingContactNameDay = upcomingContactNameDay,
                                onSaintClick = onSaintClick,
                            )
                        }
                    } else if (!uiState.contactsPermissionGranted && uiState.nameDays.isEmpty()) {
                        item {
                            ContactsPermissionCard(
                                onGrantPermissionClick = {
                                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactsPermissionCard(
    onGrantPermissionClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nächsten Kontakt-Namenstag anzeigen",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Erlaube den Kontakte-Zugriff, damit hier der nächste Namenstag aus deinen Kontakten erscheint.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
            TextButton(
                onClick = onGrantPermissionClick,
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("Kontakte freigeben")
            }
        }
    }
}

@Composable
private fun UpcomingContactNameDayCard(
    upcomingContactNameDay: UpcomingContactNameDay,
    onSaintClick: (Long) -> Unit,
) {
    val contactNameDay = upcomingContactNameDay.contactNameDay
    val matchLabel = when (contactNameDay.matchType) {
        MatchType.EXACT -> ""
        MatchType.ALIAS -> " (Alias: ${contactNameDay.contact.givenName})"
        MatchType.PHONETIC -> " (Phonetisch)"
    }
    val relativeDateLabel = when (upcomingContactNameDay.daysUntil) {
        1L -> "Morgen"
        else -> "In ${upcomingContactNameDay.daysUntil} Tagen"
    }
    val formattedDate = upcomingContactNameDay.date.format(
        DateTimeFormatter.ofPattern("d. MMMM", Locale.GERMAN),
    )

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
                Text(
                    text = "$relativeDateLabel, am $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "${upcomingContactNameDay.nextNameDay.name}$matchLabel",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { onSaintClick(upcomingContactNameDay.nextNameDay.saint.id) },
                )
            }
        }
    }
}
