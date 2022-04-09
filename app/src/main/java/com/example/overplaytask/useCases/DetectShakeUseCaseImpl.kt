package com.example.overplaytask.useCases

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.lang.Math.sqrt
import javax.inject.Inject

interface DetectShakeUseCase {
    fun initialize()
    fun getShakeUpdates(): Flow<Boolean>
    fun giveLifecycle(viewModelScope: CoroutineScope)
    fun onPause()
    fun onResume()
}

class DetectShakeUseCaseImpl @Inject constructor(
    private val context: Context
) : DetectShakeUseCase {

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var viewModelScope: CoroutineScope? = null

    private val shakeFlow = MutableSharedFlow<Boolean>()
    override fun giveLifecycle(viewModelScope: CoroutineScope) {
        this.viewModelScope = viewModelScope
    }

    override fun initialize() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager?.registerListener(
            sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 12) {
                viewModelScope?.launch {
                    shakeFlow.emit(true)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun getShakeUpdates(): Flow<Boolean> {
        return shakeFlow
    }

    override fun onPause() {
        sensorManager?.unregisterListener(sensorListener)

    }

    override fun onResume() {
        sensorManager?.registerListener(
            sensorListener, sensorManager?.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_NORMAL
        )
    }


}
