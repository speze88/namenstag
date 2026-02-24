package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.NameDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTodaysNameDaysUseCase @Inject constructor(
    private val repository: NameDayRepository,
) {
    operator fun invoke(): Flow<List<NameDay>> {
        val today = LocalDate.now()
        return repository.getNameDaysByDate(today.monthValue, today.dayOfMonth)
    }
}
