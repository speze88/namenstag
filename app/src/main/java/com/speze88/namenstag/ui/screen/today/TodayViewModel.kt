package com.speze88.namenstag.ui.screen.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.UpcomingContactNameDay
import com.speze88.namenstag.domain.usecase.GetUpcomingContactNameDaysUseCase
import com.speze88.namenstag.domain.usecase.GetTodaysNameDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodayUiState(
    val nameDays: List<NameDay> = emptyList(),
    val upcomingContactNameDays: List<UpcomingContactNameDay> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingUpcomingContacts: Boolean = false,
    val contactsPermissionGranted: Boolean = false,
    val showContactsPermissionRationale: Boolean = false,
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodaysNameDays: GetTodaysNameDaysUseCase,
    private val getUpcomingContactNameDays: GetUpcomingContactNameDaysUseCase,
    private val repository: NameDayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
            getTodaysNameDays().collect { nameDays ->
                _uiState.value = _uiState.value.copy(
                    nameDays = nameDays,
                    isLoading = false,
                )
            }
        }
    }

    fun onContactsPermissionGranted() {
        if (_uiState.value.contactsPermissionGranted && _uiState.value.upcomingContactNameDays.isNotEmpty()) {
            return
        }

        _uiState.value = _uiState.value.copy(
            contactsPermissionGranted = true,
            showContactsPermissionRationale = false,
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingUpcomingContacts = true)
            val upcomingContactNameDays = getUpcomingContactNameDays()
            _uiState.value = _uiState.value.copy(
                upcomingContactNameDays = upcomingContactNameDays,
                isLoadingUpcomingContacts = false,
            )
        }
    }

    fun onContactsPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            contactsPermissionGranted = false,
            showContactsPermissionRationale = true,
            isLoadingUpcomingContacts = false,
        )
    }

    fun dismissContactsPermissionRationale() {
        _uiState.value = _uiState.value.copy(showContactsPermissionRationale = false)
    }
}
