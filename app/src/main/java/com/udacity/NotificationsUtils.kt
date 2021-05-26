package com.udacity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val REQUEST_CODE = 1
private const val FLAGS = 0
private const val CHANNEL_ID = "com.udacity.load-app.common"

fun NotificationManagerCompat.send(
    applicationContext: Context,
    title: String,
    message: String
): Notification {
    val intent = Intent(applicationContext, DetailActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(applicationContext, REQUEST_CODE, intent, FLAGS)

    return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setStyle(NotificationCompat.BigPictureStyle())
        .setColorized(true)
        .setColor(Color.RED)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setChannelId(CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.check_the_status),
            pendingIntent
        )
        .setCategory(NotificationCompat.CATEGORY_PROGRESS)
        .setAutoCancel(true)
        .build()
}

fun NotificationManagerCompat.createChannel(applicationContext: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = applicationContext.getString(R.string.channel_name)
        val descriptionText = applicationContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        createNotificationChannel(channel)
    }
}
