package com.example.kotlinspeedometer

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.github.anastr.speedviewlib.PointerSpeedometer
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var speedometerView: SpeedometerView
    private lateinit var pointerSpeedometer: PointerSpeedometer // New: PointerSpeedometer
    private lateinit var animationView: LottieAnimationView // New: LottieAnimationView
    private lateinit var velocimeterContainer: FrameLayout // New: Container for speedometers
    private lateinit var switchVelocimeterButton: Button // New: Button to switch speedometers
    private lateinit var motorcycleButton: Button
    private var isOriginalVelocimeter = true // New: Track which speedometer is active

    private var velocity = 0.0
    private var acceleration = 0.0
    private val maxSpeed = 200.0
    private val baseAccelerationRate = 10.0 // Base units per second^2
    private val decelerationRate = -100.0 // Units per second^2
    private val naturalDecelerationRate = -20.0 // when no pedal is pressed
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        velocimeterContainer = findViewById(R.id.velocimeterContainer) // New: Initialize container
        speedometerView = findViewById(R.id.speedometerView)
        pointerSpeedometer = findViewById(R.id.pointerSpeedometer) // New: Initialize PointerSpeedometer
        pointerSpeedometer.maxSpeed = maxSpeed.toFloat() // Set max speed dynamically
        pointerSpeedometer.withTremble = false;
        animationView = findViewById(R.id.animationView) // New: Initialize LottieAnimationView
        animationView.repeatCount = ValueAnimator.INFINITE
        switchVelocimeterButton = findViewById(R.id.switchVelocimeterButton) // New: Initialize switch button

        animationView.playAnimation()

        switchVelocimeterButton.setOnClickListener { // New: Set up switch button listener
            switchVelocimeter()
        }

        val accelerationPedal: Button = findViewById(R.id.accelerationPedal)
        val decelerationPedal: Button = findViewById(R.id.decelerationPedal)

        updateRunnable = object : Runnable {
            override fun run() {
                updateVelocity()
                if (isOriginalVelocimeter) {
                    speedometerView.setSpeed(velocity.toInt())
                } else {
                    pointerSpeedometer.speedTo(velocity.toFloat(), 0) // New: Update PointerSpeedometer
                }
                updateAnimationSpeed() // New: Update animation speed
                handler.postDelayed(this, 50) // Update every 50 milliseconds
            }
        }

        handler.post(updateRunnable) // Start the runnable immediately

        accelerationPedal.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    acceleration = baseAccelerationRate
                    increaseAcceleration()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> acceleration = naturalDecelerationRate
            }
            true
        }

        decelerationPedal.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> acceleration = decelerationRate
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> acceleration = naturalDecelerationRate
            }
            true
        }
    }

    private fun switchVelocimeter() { // New: Function to switch speedometers
        if (isOriginalVelocimeter) {
            speedometerView.visibility = View.GONE
            pointerSpeedometer.visibility = View.VISIBLE
        } else {
            speedometerView.visibility = View.VISIBLE
            pointerSpeedometer.visibility = View.GONE
        }
        isOriginalVelocimeter = !isOriginalVelocimeter
    }

    private fun updateVelocity() {
        val deltaTime = 0.05 // Time step in seconds 50 ms

        if (acceleration == 0.0) {
            acceleration = decelerationRate
        }

        velocity += acceleration * deltaTime
        velocity = min(max(velocity, 0.0), maxSpeed) // Ensure velocity stays within bounds

        // Reset acceleration if natural deceleration is applied
        if (acceleration == decelerationRate) {
            acceleration = 0.0
        }
    }

    private fun increaseAcceleration() {
        handler.postDelayed({
            if (acceleration > 0) {
                acceleration += baseAccelerationRate // Increase acceleration gradually
                increaseAcceleration()
            }
        }, 1000) // Increase acceleration every second
    }

    private fun updateAnimationSpeed() { // New: Function to update animation speed
        val animationSpeed = (velocity / maxSpeed).toFloat() * 2
        animationView.speed = animationSpeed

    }
}
