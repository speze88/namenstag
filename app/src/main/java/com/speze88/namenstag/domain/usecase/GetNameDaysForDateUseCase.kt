package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNameDaysForDateUseCase @Inject constructor(
    private val repository: NameDayRepository,
) {
    operator fun invoke(month: Int, day: Int): Flow<List<NameDay>> {
        return repository.getNameDaysByDate(month, day)
    }
}
