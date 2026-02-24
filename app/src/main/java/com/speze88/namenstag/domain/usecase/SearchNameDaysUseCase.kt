package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchNameDaysUseCase @Inject constructor(
    private val repository: NameDayRepository,
) {
    operator fun invoke(query: String): Flow<List<NameDay>> {
        if (query.isBlank()) return flowOf(emptyList())
        return repository.searchNameDays(query.trim())
    }
}
