package com.speze88.namenstag.ui.screen.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.calendar.CalendarSyncManager
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.usecase.GetContactsWithNameDaysUseCase
import com.speze88.namenstag.domain.usecase.SetPreferredContactNameDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactsUiState(
    val contactNameDays: List<ContactNameDay> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncingCalendar: Boolean = false,
    val permissionGranted: Boolean = false,
    val showPermissionRationale: Boolean = false,
    val calendarSyncMessage: String? = null,
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsWithNameDays: GetContactsWithNameDaysUseCase,
    private val setPreferredContactNameDay: SetPreferredContactNameDayUseCase,
    private val calendarSyncManager: CalendarSyncManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    fun onPermissionGranted() {
        _uiState.value = _uiState.value.copy(permissionGranted = true)
        loadContacts()
    }

    fun onPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            permissionGranted = false,
            showPermissionRationale = true,
        )
    }

    fun dismissRationale() {
        _uiState.value = _uiState.value.copy(showPermissionRationale = false)
    }

    fun clearCalendarSyncMessage() {
        _uiState.value = _uiState.value.copy(calendarSyncMessage = null)
    }

    fun selectPreferredNameDay(contactId: String, selectedNameDay: NameDay?, availableNameDays: List<NameDay>) {
        viewModelScope.launch {
            setPreferredContactNameDay(contactId, selectedNameDay, availableNameDays)
            val updatedContacts = getContactsWithNameDays()
            _uiState.value = _uiState.value.copy(contactNameDays = updatedContacts)
        }
    }

    fun syncContactsToCalendar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncingCalendar = true,
                calendarSyncMessage = null,
            )
            runCatching {
                calendarSyncManager.syncContactNameDays(_uiState.value.contactNameDays)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSyncingCalendar = false,
                    calendarSyncMessage = "Namenstage wurden in den Kalender synchronisiert.",
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isSyncingCalendar = false,
                    calendarSyncMessage = "Kalender-Synchronisation fehlgeschlagen.",
                )
            }
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                calendarSyncMessage = null,
            )
            val contactNameDays = getContactsWithNameDays()
            _uiState.value = _uiState.value.copy(
                contactNameDays = contactNameDays,
                isLoading = false,
            )
        }
    }
}
