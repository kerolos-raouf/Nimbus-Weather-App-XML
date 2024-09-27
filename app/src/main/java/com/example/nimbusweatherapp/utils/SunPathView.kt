package com.example.nimbusweatherapp.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs

class SunPathView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = 0xFFFFD700.toInt()
        strokeWidth = 2f
        style = Paint.Style.FILL
    }

    private val paintLine = Paint().apply {
        color = 0xFFFFD700.toInt()
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val path = Path()
    private var sunX = 0f
    private var sunY = 0f

    init {
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(-1f, 1f).apply {
            duration = 10000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {anim->
                val fraction = anim.animatedValue as Float
                sunX = width * abs(fraction)
                val currentHeight = height/2f
                sunY = currentHeight - ((currentHeight/2) * abs(fraction))
                //Log.d("Kerolos", "startAnimation: $sunY")
                invalidate()
            }
            start()
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path.reset()
        path.moveTo(0f, (height / 2).toFloat())
        path.quadTo(width / 2f, 0f, width.toFloat(), (height / 2).toFloat())

        canvas.drawPath(path, paintLine)


        canvas.drawCircle(sunX, sunY, 30f, paint)
    }

}