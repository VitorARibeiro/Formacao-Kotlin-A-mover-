package com.example.velocimetro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    private lateinit var speedometerView: SpeedometerView
    private lateinit var motorcycleImage: ImageView
    private var acceleration = 0.0
    private var speed = 0.0
    private val maxSpeed = 200.0
    private val accelerationRate = 10.0
    private val decelerationRate = -50.0
    private val naturalDecelerationRate = -20.0 // when no pedal is pressed
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        speedometerView = findViewById(R.id.speedometerView)
        motorcycleImage = findViewById(R.id.motorcycleImage)

        val increase : Button = findViewById(R.id.increase)
        val decrease : Button = findViewById(R.id.decrease)

        updateRunnable = object : Runnable {
            override fun run() {
                updateVelocity()
                speedometerView.setSpeed(speed.toInt())
                handler.postDelayed(this, 50) // Update every 50ms
            }
        }

        handler.post(updateRunnable) // Start the runnable

        increase.text = getString(R.string.increase_button_name)
        decrease.text = getString(R.string.decrease_button_name)

        increase.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    acceleration = accelerationRate
                    increaseAcceleration()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> acceleration = 0.0
            }
            true
        }

        decrease.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> acceleration = decelerationRate
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> acceleration = 0.0
            }
            true
        }
    }

    private fun updateVelocity() {
        val deltaTime = 0.05 // Time step in seconds 50 ms

        if (acceleration == 0.0) {
            acceleration = naturalDecelerationRate
        }

        speed += acceleration * deltaTime
        speed = min(max(speed, 0.0), maxSpeed) // Ensure velocity stays within bounds

        // Reset acceleration if natural deceleration is applied
        if (acceleration == naturalDecelerationRate) {
            acceleration = 0.0
        }
    }

    private fun increaseAcceleration() {
        handler.postDelayed({
            if (acceleration > 0) {
                acceleration += accelerationRate // Increase acceleration gradually
                increaseAcceleration()
            }
        }, 1000) // Increase acceleration every second
    }


}