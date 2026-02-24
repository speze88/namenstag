package com.speze88.namenstag.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.speze88.namenstag.MainActivity
import com.speze88.namenstag.R
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.NameDay
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameDayNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        const val CHANNEL_ID = "namenstag_daily"
        const val NOTIFICATION_ID = 1001
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Tägliche Namenstage",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Benachrichtigungen über heutige Namenstage deiner Kontakte"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNameDayNotification(contactNameDays: List<ContactNameDay>) {
        if (contactNameDays.isEmpty()) return

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val names = contactNameDays.joinToString(", ") { it.contact.givenName }
        val title = if (contactNameDays.size == 1) {
            "Namenstag: ${contactNameDays.first().contact.givenName}"
        } else {
            "${contactNameDays.size} Kontakte haben heute Namenstag"
        }

        val text = if (contactNameDays.size == 1) {
            val cnd = contactNameDays.first()
            val saintName = cnd.nameDays.firstOrNull()?.saint?.canonicalName ?: cnd.contact.givenName
            "Heute ist der Namenstag von $saintName. ${cnd.nameDays.firstOrNull()?.saint?.description?.take(100) ?: ""}"
        } else {
            "Heute haben Namenstag: $names"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}
