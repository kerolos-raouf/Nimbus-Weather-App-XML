package com.example.nimbusweatherapp.alertFragment.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.utils.Constants

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val alertAction = intent?.action

        when(alertAction)
        {
            Constants.ALERT_ACTION_NOTIFICATION -> {
                showNotification(context)
            }
            Constants.ALERT_ACTION_ALARM -> {
                showAlarm(context)
            }
        }

    }

    private fun createChannel(context: Context?)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_NAME,
                Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun showNotification(context: Context?)
    {
        createChannel(context)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_NAME)
            .setContentTitle("Weather Alert")
            .setContentText("Let's check the weather.")
            .setSmallIcon(R.drawable.ic_sunny)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Constants.NOTIFICATION_CHANNEL_ID,notification)

    }

    private fun showAlarm(context: Context?) {
        //val ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        //ringtone.play()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = Constants.ALERT_ACTION_START_ALARM
        context?.sendBroadcast(intent)
    }
}