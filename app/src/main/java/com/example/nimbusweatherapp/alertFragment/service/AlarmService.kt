package com.example.nimbusweatherapp.alertFragment.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.nimbusweatherapp.R
import kotlin.math.log

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (mediaPlayer == null) {

            mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
            mediaPlayer?.apply {
                isLooping = true
                start()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        fun startAlarm(context: Context) {
            val intent = Intent(context, AlarmService::class.java)
            context.startService(intent)
        }

        fun stopAlarm(context: Context) {
            val intent = Intent(context, AlarmService::class.java)
            context.stopService(intent)
        }
    }
}