package com.example.taller1_componentes.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.roundToInt

class OrientationSensor(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val magnetometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val gravity = FloatArray(3)
    private val magnetic = FloatArray(3)

    var direction: Int = 0
        private set

    private var hasGravity = false
    private var hasMagnetic = false

    override fun onSensorChanged(event: SensorEvent?) {

        if (event == null) return

        when (event.sensor.type) {

            Sensor.TYPE_ACCELEROMETER -> {
                gravity[0] = event.values[0]
                gravity[1] = event.values[1]
                gravity[2] = event.values[2]
                hasGravity = true
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                magnetic[0] = event.values[0]
                magnetic[1] = event.values[1]
                magnetic[2] = event.values[2]
                hasMagnetic = true
            }
        }

        if (hasGravity && hasMagnetic) {

            val rotationMatrix = FloatArray(9)
            val orientation = FloatArray(3)

            val success = SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                gravity,
                magnetic
            )

            if (success) {

                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )

                var degrees =
                    Math.toDegrees(orientation[0].toDouble()).toFloat()

                if (degrees < 0) {
                    degrees += 360f
                }

                direction = degrees.roundToInt()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        magnetometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}