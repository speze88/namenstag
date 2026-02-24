package com.speze88.namenstag.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.usecase.SearchNameDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<NameDay> = emptyList(),
    val hasSearched: Boolean = false,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNameDays: SearchNameDaysUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), hasSearched = false)
            return
        }
        searchJob = viewModelScope.launch {
            searchNameDays(query).collect { results ->
                _uiState.value = _uiState.value.copy(
                    results = results,
                    hasSearched = true,
                )
            }
        }
    }
}
