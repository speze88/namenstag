package com.speze88.namenstag.ui.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.usecase.GetNameDaysForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val nameDaysForSelectedDate: List<NameDay> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getNameDaysForDate: GetNameDaysForDateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private var collectJob: Job? = null

    init {
        selectDate(LocalDate.now())
    }

    fun selectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            isLoading = true,
        )
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            getNameDaysForDate(date.monthValue, date.dayOfMonth).collect { nameDays ->
                _uiState.value = _uiState.value.copy(
                    nameDaysForSelectedDate = nameDays,
                    isLoading = false,
                )
            }
        }
    }

    fun changeMonth(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(currentMonth = yearMonth)
    }

    fun previousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
    }

    fun nextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
    }
}
