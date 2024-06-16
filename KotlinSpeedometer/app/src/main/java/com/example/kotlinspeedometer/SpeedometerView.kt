package com.example.kotlinspeedometer

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var speed: Int = 0
    private val speedTextView: TextView
    private val speedometerNeedle: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.speedometer_view, this, true)
        speedTextView = findViewById(R.id.speedTextView)
        speedometerNeedle = findViewById(R.id.speedometerNeedle)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        speedometerNeedle.pivotX = speedometerNeedle.width / 2f
        speedometerNeedle.pivotY = speedometerNeedle.height * 0.625f
    }


    fun setSpeed(newSpeed: Int) {
        speed = newSpeed.coerceIn(0, 360) // Assuming max speed is 240 km/h
        speedTextView.text = "$speed km/h"
        updateNeedle()
    }

    private fun updateNeedle() {
        val needleRotation = speed *1.0f - 100 // Assuming needle starts at -120 degrees
        val animator = ObjectAnimator.ofFloat(speedometerNeedle, "rotation", speedometerNeedle.rotation, needleRotation)
        animator.duration = 0
        animator.start()
    }
}