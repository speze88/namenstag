package com.speze88.namenstag.ui.screen.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.usecase.GetContactsWithNameDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactsUiState(
    val contactNameDays: List<ContactNameDay> = emptyList(),
    val isLoading: Boolean = false,
    val permissionGranted: Boolean = false,
    val showPermissionRationale: Boolean = false,
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsWithNameDays: GetContactsWithNameDaysUseCase,
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

    private fun loadContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val contactNameDays = getContactsWithNameDays()
            _uiState.value = _uiState.value.copy(
                contactNameDays = contactNameDays,
                isLoading = false,
            )
        }
    }
}
