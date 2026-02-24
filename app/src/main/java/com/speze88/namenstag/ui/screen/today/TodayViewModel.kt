package com.speze88.namenstag.ui.screen.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.usecase.GetTodaysNameDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodayUiState(
    val nameDays: List<NameDay> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodaysNameDays: GetTodaysNameDaysUseCase,
    private val repository: NameDayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
            getTodaysNameDays().collect { nameDays ->
                _uiState.value = TodayUiState(
                    nameDays = nameDays,
                    isLoading = false,
                )
            }
        }
    }
}
