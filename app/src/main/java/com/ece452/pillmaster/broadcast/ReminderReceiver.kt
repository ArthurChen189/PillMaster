package com.ece452.pillmaster.broadcast

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ece452.pillmaster.MainActivity
import com.ece452.pillmaster.R
import java.util.Date


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager =
            context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val dbId = intent?.getLongExtra("id", -1) ?: -1
        val title = intent?.getStringExtra("title") ?: "Pill Master"
        val time = intent?.getStringExtra("msg")?:"Take Your Today Pill"

        val icon = R.drawable.pill
        val fullScreenIntent = Intent(context.applicationContext, MainActivity::class.java)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, dbId.toInt(),
            fullScreenIntent, PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("PillMasterReminder", "PillMaster Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setDescription("Pill Reminder Channel")
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(context, "Reminder")
//            .setContentIntent(contentIntent)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(time)
            .setPriority(NotificationCompat.VISIBILITY_PUBLIC)
            .setColor(Color.GREEN)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(alarmSound)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        with(NotificationManagerCompat.from(context.applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(getID(), notification)
        }
    }

    // to get a unique id for each notification
    fun getID(): Int = (Date().time / 1000L % Integer.MAX_VALUE).toInt()

}
