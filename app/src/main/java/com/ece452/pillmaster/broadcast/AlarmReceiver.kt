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
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ece452.pillmaster.MainActivity
import com.ece452.pillmaster.R
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager =
            context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        var isShow = intent?.getIntExtra("isShow", 0) ?: 0
        val dbId = intent?.getLongExtra("id", -1) ?: -1
        val title = intent?.getStringExtra("title") ?: "Pill Master"
        val time = intent?.getStringExtra("msg")?:"Take Your Today Pill"

        val icon = R.drawable.ic_launcher_background
        val fullScreenIntent = Intent(context.applicationContext, MainActivity::class.java)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, dbId.toInt(),
            fullScreenIntent, PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("Reminder", "My Notifications", NotificationManager.IMPORTANCE_HIGH)
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description")
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
//        val notificationIntent = Intent(context, MainActivity::class.java)
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val contentIntent= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // start activity from notification
//            PendingIntent.getActivity(
//                context,
//                0,
//                notificationIntent,
//                PendingIntent.FLAG_MUTABLE
//            )
//        } else {
//            PendingIntent.getActivity(
//                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        }
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
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

//        notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//        notificationManager.notify(getNumber(), notification)

        with(NotificationManagerCompat.from(context.applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(getNumber(), notification)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED")
//        registerReceiver(alarmReceiver, filter)
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
//        notificationManager!!.cancel(NOTIFICATION_ID)
//    }
//
//    protected fun onStop() {
//        super.onStop()
//        unregisterReceiver(alarmReceiver)
//    }
    // to show multiple number of notification , there is need of unique number
    fun getNumber(): Int = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
    /*var randomNumber = Random()
    var m = randomNumber.nextInt(9999 - 1000) + 1000
    */
}
