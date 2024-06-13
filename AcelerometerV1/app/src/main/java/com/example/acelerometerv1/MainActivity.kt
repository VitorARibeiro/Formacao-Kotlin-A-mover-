package com.example.acelerometerv1

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.github.anastr.speedviewlib.PointerSpeedometer


class MainActivity : AppCompatActivity() {

    private lateinit var pointerSpeedometer : PointerSpeedometer
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView
    private lateinit var animationView: LottieAnimationView
    private var slowdownAnimator: ValueAnimator? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointerSpeedometer = findViewById(R.id.pointerSpeedometer)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)
        animationView = findViewById(R.id.animationView)

        //Remover o tremble do acelerador
        pointerSpeedometer.withTremble = false;
        animationView.speed = 0f
        animationView.playAnimation()



        // Set a listener to the SeekBar to update the SpeedView and display the value
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the SpeedView with the current progress
                pointerSpeedometer.speedTo(progress.toFloat())

                // Display the SeekBar value
                seekBarValue.text = getString(R.string.seek_bar_value, progress)

                if(progress != 0) {

                    accelerateAnimation((progress * 0.02).toFloat())
                }else{
                    pauseAnimationWithSlowdown()

                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this implementation

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBarValue.text = "0";
                seekBar?.setProgress(0,true)
            }
        })
    }

    private fun pauseAnimationWithSlowdown() {
        // Cancel any ongoing slowdown animation
        slowdownAnimator?.cancel()

        // Capture the current speed
        val currentSpeed = animationView.speed

        // Create a ValueAnimator to animate the speed change
        slowdownAnimator = ValueAnimator.ofFloat(currentSpeed, 0.0f).apply {
            duration = 4000 // 4 seconds
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                animationView.setSpeed(animatedValue)
                // Pause the animation when speed reaches 0
                if (animatedValue == 0.0f) {
                    animationView.pauseAnimation()
                }
            }
            start()
        }
    }

    private fun accelerateAnimation(speed: Float) {
        // Cancel any ongoing slowdown animation
        slowdownAnimator?.cancel()

        // Set the desired speed and start the animation if not already playing
        animationView.speed = speed
        if (!animationView.isAnimating) {
            animationView.playAnimation()
        }
    }
}
