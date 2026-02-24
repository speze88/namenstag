package com.speze88.namenstag.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        const val WORK_NAME = "daily_nameday_check"
        val TARGET_TIME: LocalTime = LocalTime.of(7, 0)
    }

    fun initialize() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val initialDelay = calculateInitialDelay()

        val workRequest = PeriodicWorkRequestBuilder<DailyNameDayWorker>(
            1, TimeUnit.DAYS,
        )
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }

    private fun calculateInitialDelay(): Long {
        val now = LocalDateTime.now()
        var nextRun = now.toLocalDate().atTime(TARGET_TIME)
        if (now >= nextRun) {
            nextRun = nextRun.plusDays(1)
        }
        return Duration.between(now, nextRun).toMillis()
    }
}
