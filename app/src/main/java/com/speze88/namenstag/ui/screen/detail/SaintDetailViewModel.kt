package com.speze88.namenstag.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SaintDetailUiState(
    val saint: Saint? = null,
    val nameDays: List<NameDay> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class SaintDetailViewModel @Inject constructor(
    private val repository: NameDayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaintDetailUiState())
    val uiState: StateFlow<SaintDetailUiState> = _uiState.asStateFlow()

    fun loadSaint(saintId: Long) {
        viewModelScope.launch {
            repository.getSaintById(saintId).collect { saint ->
                if (saint != null) {
                    repository.getNameDaysByName(saint.canonicalName).collect { nameDays ->
                        _uiState.value = SaintDetailUiState(
                            saint = saint,
                            nameDays = nameDays,
                            isLoading = false,
                        )
                    }
                } else {
                    _uiState.value = SaintDetailUiState(isLoading = false)
                }
            }
        }
    }
}
