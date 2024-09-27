package com.example.nimbusweatherapp.alertFragment.alertDismissActivity

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.alertFragment.service.AlarmService
import com.example.nimbusweatherapp.databinding.ActivityAlertDismissBinding
import kotlin.random.Random

class OverlayAlertView {
    private var binding: ActivityAlertDismissBinding? = null
    private var windowManager: WindowManager? = null

    val weatherQuotes = listOf(
        "The weather is a great metaphor for life—sometimes it’s good, sometimes it’s bad, and there’s nothing much you can do about it but carry an umbrella.\" – Pepper Giardino",
        "\"No matter how rough the weather is, the sun always comes out.\" – Unknown",
        "\"There is no such thing as bad weather, only different kinds of good weather.\" – John Ruskin",
        "\"Wherever you go, no matter what the weather, always bring your own sunshine.\" – Anthony J. D'Angelo",
        "\"The storm is a good opportunity for the pine and the cypress to show their strength and their stability.\" – Ho Chi Minh",
        "\"The weather doesn’t matter as long as you are with the right people.\" – Unknown",
        "\"Rain showers my spirit and waters my soul.\" – Emily Logan Decens",
        "\"Nature is so powerful, so strong. Capturing its essence is not easy—your work becomes a dance with light and the weather.\" – Annie Leibovitz",
        "\"There’s no such thing as bad weather, just soft people.\" – Bill Bowerman",
        "\"Sunshine is delicious, rain is refreshing, wind braces us up, snow is exhilarating; there is really no such thing as bad weather, only different kinds of good weather.\" – John Ruskin",
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun show(context: Context)
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.activity_alert_dismiss, null))

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.TOP

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager?.addView(binding?.root, layoutParams)

        binding?.dismissButton?.setOnClickListener {

            AlarmService.stopAlarm(context)

            removeTheOverlay()
        }

        binding?.dismissQuoteText?.text = weatherQuotes[Random.nextInt(0, weatherQuotes.size)]

    }

    fun removeTheOverlay()
    {
        if (binding != null && windowManager != null) {
            windowManager?.removeView(binding!!.root)
            binding = null
        }
    }


}