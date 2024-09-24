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

class OverlayAlertView {
    private var binding: ActivityAlertDismissBinding? = null
    private var windowManager: WindowManager? = null

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

    }

    fun removeTheOverlay()
    {
        if (binding != null && windowManager != null) {
            windowManager?.removeView(binding!!.root)
            binding = null
        }
    }


}