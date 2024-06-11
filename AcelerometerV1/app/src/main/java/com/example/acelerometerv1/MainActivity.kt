package com.example.acelerometerv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import com.github.anastr.speedviewlib.PointerSpeedometer


class MainActivity : AppCompatActivity() {

    private lateinit var pointerSpeedometer : PointerSpeedometer
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointerSpeedometer = findViewById(R.id.pointerSpeedometer)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)
        pointerSpeedometer.withTremble = false;

        // Set a listener to the SeekBar to update the SpeedView and display the value
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the SpeedView with the current progress
                pointerSpeedometer.speedTo(progress.toFloat())

                // Display the SeekBar value
                seekBarValue.text = getString(R.string.seek_bar_value, progress)
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
}
