package com.example.nimbusweatherapp.alertFragment.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.nimbusweatherapp.alertFragment.alertDismissActivity.OverlayAlertView
import com.example.nimbusweatherapp.alertFragment.service.AlarmService
import com.example.nimbusweatherapp.utils.Constants

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val overlayAlertView = OverlayAlertView()

        when (intent?.action) {
            Constants.ALERT_ACTION_START_ALARM -> {
                context?.let {
                    AlarmService.startAlarm(it)
                    overlayAlertView.show(it)
                }
            }
            Constants.ALERT_ACTION_STOP_ALARM -> {
                context?.let {
                    AlarmService.stopAlarm(it)
                    overlayAlertView.removeTheOverlay()
                }
            }
        }
    }
}