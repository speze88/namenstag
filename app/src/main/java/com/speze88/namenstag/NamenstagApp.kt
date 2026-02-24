package com.speze88.namenstag

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.speze88.namenstag.notification.NameDayNotificationManager
import com.speze88.namenstag.worker.WorkManagerInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NamenstagApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManagerInitializer: WorkManagerInitializer

    @Inject
    lateinit var notificationManager: NameDayNotificationManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        notificationManager.createNotificationChannel()
        workManagerInitializer.initialize()
    }
}
