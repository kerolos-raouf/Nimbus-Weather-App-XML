package com.example.nimbusweatherapp.alertFragment.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AlertReceiver : BroadcastReceiver() {


    @Inject
    lateinit var repository: Repository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {

        val alertAction = intent?.action
        val alert = intent?.getParcelableExtra<Alert>(Constants.ALERT_KEY)


        val showNotification = context?.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)?.getBoolean(Constants.NOTIFICATION_KEY, true)
        if(showNotification == true)
        {
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


        alert?.let {
            GlobalScope.launch {
                repository.deleteAlert(alert)
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
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        createChannel(context)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_NAME)
            .setContentTitle("Nimbus")
            .setContentText("Let's check the weather")
            .setSmallIcon(R.drawable.ic_sunny)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
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