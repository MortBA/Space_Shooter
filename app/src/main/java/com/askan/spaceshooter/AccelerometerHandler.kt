package com.askan.spaceshooter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

// Create a class that handles accelerometer sensor data
class AccelerometerHandler(private val context: Context) : SensorEventListener {
    // Initialize the SensorManager
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Initialize a variable to store the X-axis acceleration
    private var accelerationX = 0.0f

    // Constructor, register the accelerometer listener
    init {
        registerAccelerometerListener()
    }

    // Register the accelerometer listener with SENSOR_DELAY_GAME
    private fun registerAccelerometerListener() {
        // Get the default accelerometer sensor
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Check if the accelerometer is available
        accelerometer?.let {
            // Register this class as the listener for accelerometer events
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    // This function is called when the accelerometer data changes
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // Store the X-axis acceleration value
            accelerationX = event.values[0]
            // You can use the accelerationX value to control your entity's movement
        }
    }

    // This function is called when the accuracy of the sensor changes
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    // Unregister the sensor listener to release resources
    fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

    // Get the X-axis acceleration value
    fun getAccelerationX(): Float {
        return accelerationX
    }
}
