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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointerSpeedometer = findViewById(R.id.pointerSpeedometer)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)
        animationView = findViewById(R.id.animationView)

        //Remover o tremble do acelerador
        pointerSpeedometer.withTremble = false;




        // Set a listener to the SeekBar to update the SpeedView and display the value
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the SpeedView with the current progress
                pointerSpeedometer.speedTo(progress.toFloat())

                // Display the SeekBar value
                seekBarValue.text = getString(R.string.seek_bar_value, progress)

                if(progress != 0) {
                    animationView.resumeAnimation()
                    animationView.speed = (progress * 0.02).toFloat()
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
        // Create a ValueAnimator to animate the speed change
        val valueAnimator = ValueAnimator.ofFloat(animationView.speed, 0.0f)
        valueAnimator.duration = 4000 // 4 seconds
        valueAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            animationView.setSpeed(animatedValue)
            // Pause the animation when speed reaches 0
            if (animatedValue == 0.0f) {
                animationView.pauseAnimation()
            }
        }
        valueAnimator.start()
    }
}
