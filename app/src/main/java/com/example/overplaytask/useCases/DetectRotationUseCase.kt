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
import javax.inject.Inject

interface DetectRotationUseCase {
    fun initialize()
    fun getRotationUpdates() : Flow<TaskToPerform>
    fun giveLifecycle(viewModelScope: CoroutineScope)
    fun onPause()
    fun onResume()

    enum class TaskToPerform{
        FORWARD,PREVIOUS , VOLUME_UP, VOLUME_DOWN , NONE
    }
}

class DetectRotationUseCaseImpl @Inject constructor(
    private val context: Context
) : DetectRotationUseCase {

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var viewModelScope: CoroutineScope? = null

    private val shakeFlow = MutableSharedFlow<DetectRotationUseCase.TaskToPerform>()
    override fun giveLifecycle(viewModelScope: CoroutineScope) {
        this.viewModelScope = viewModelScope
    }
    override fun initialize() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager?.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)

        acceleration = 10f
    }

    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4) { 0f }
    private var timestamp: Float = 0f

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

                val dT = (event.timestamp - timestamp) * NS2S
                // Axis of the rotation sample, not normalized yet.
                var axisX: Float = event.values[0]
                var axisY: Float = event.values[1]
                var axisZ: Float = event.values[2]

            if (kotlin.math.abs(axisX) < kotlin.math.abs(axisZ)){
                if(axisZ > 0.8f) { // anticlockwise
                    viewModelScope?.launch {
                        shakeFlow.emit(DetectRotationUseCase.TaskToPerform.PREVIOUS)
                    }
                }
                else if(axisZ < -0.8f) { // clockwise
                    viewModelScope?.launch {
                        shakeFlow.emit(DetectRotationUseCase.TaskToPerform.FORWARD)
                    }
                }
            }
            else{
                if(axisX > 0.8f) {
                    viewModelScope?.launch {
                        shakeFlow.emit(DetectRotationUseCase.TaskToPerform.VOLUME_UP)
                    }
                }
                else if(axisX < -0.8f) {
                    viewModelScope?.launch {
                        shakeFlow.emit(DetectRotationUseCase.TaskToPerform.VOLUME_DOWN)
                    }
                }
            }


        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun getRotationUpdates(): Flow<DetectRotationUseCase.TaskToPerform> {
        return shakeFlow
    }

    override fun onPause() {
        sensorManager?.unregisterListener(sensorListener)

    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager?.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
    }


}
