package com.speze88.namenstag.ui.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speze88.namenstag.ui.component.NameDayCard
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onSaintClick: (Long) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kalender") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )

        // Month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Vorheriger Monat")
            }
            Text(
                text = uiState.currentMonth.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.GERMAN),
                ),
                style = MaterialTheme.typography.titleLarge,
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Nächster Monat")
            }
        }

        // Day of week headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
        ) {
            val daysOfWeek = listOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY,
            )
            daysOfWeek.forEach { day ->
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.GERMAN),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Calendar grid
        val yearMonth = uiState.currentMonth
        val firstOfMonth = yearMonth.atDay(1)
        val dayOfWeekOffset = (firstOfMonth.dayOfWeek.value - 1) // Monday = 0
        val daysInMonth = yearMonth.lengthOfMonth()

        val calendarDays = buildList {
            repeat(dayOfWeekOffset) { add(null) }
            for (day in 1..daysInMonth) {
                add(yearMonth.atDay(day))
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(calendarDays) { date ->
                if (date == null) {
                    Box(modifier = Modifier.aspectRatio(1f))
                } else {
                    val isSelected = date == uiState.selectedDate
                    val isToday = date == LocalDate.now()
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) {
                                    Modifier.background(
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape,
                                    )
                                } else if (isToday) {
                                    Modifier.background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        CircleShape,
                                    )
                                } else {
                                    Modifier
                                },
                            )
                            .clickable { viewModel.selectDate(date) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }
            }
        }

        // Name days for selected date
        val selectedDateFormatted = uiState.selectedDate.format(
            DateTimeFormatter.ofPattern("d. MMMM", Locale.GERMAN),
        )

        Text(
            text = "Namenstage am $selectedDateFormatted",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        if (uiState.nameDaysForSelectedDate.isEmpty() && !uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Keine Namenstage an diesem Tag.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(uiState.nameDaysForSelectedDate, key = { it.name }) { nameDay ->
                    NameDayCard(
                        nameDay = nameDay,
                        onClick = { onSaintClick(nameDay.saint.id) },
                    )
                }
            }
        }
    }
}
