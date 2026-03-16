package com.speze88.namenstag.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.usecase.GetTodaysContactNameDaysUseCase
import com.speze88.namenstag.notification.NameDayNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class DailyNameDayWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val nameDayRepository: NameDayRepository,
    private val getTodaysContactNameDays: GetTodaysContactNameDaysUseCase,
    private val notificationManager: NameDayNotificationManager,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            nameDayRepository.seedDatabaseIfEmpty()
            val contactNameDays = getTodaysContactNameDays(LocalDate.now())

            notificationManager.showNameDayNotification(contactNameDays)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
