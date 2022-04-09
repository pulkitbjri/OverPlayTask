package com.example.overplaytask.ui.frags

import androidx.lifecycle.viewModelScope
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.useCases.DetectRotationUseCase
import com.example.overplaytask.useCases.DetectShakeUseCaseImpl
import com.example.overplaytask.useCases.LastLocationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

abstract class MainFragmentViewModel : BaseViewModel() {
    data class ViewData(
        val restartVideo: Boolean = false,
        val valPauseVideo: Boolean = false,
        val rotationData: DetectRotationUseCase.TaskToPerform = DetectRotationUseCase.TaskToPerform.NONE,
    )

    abstract val dataFlow: StateFlow<ViewData>
    abstract fun initLocation()

}

class MainFragmentViewModelImpl @Inject constructor(
    val lastLocationUseCase: LastLocationUseCase,
    val detectShakeUseCaseImpl: DetectShakeUseCaseImpl,
    val detectRotationUseCase: DetectRotationUseCase,
) : MainFragmentViewModel() {
    override val dataFlow = MutableStateFlow(ViewData())

    init {

        detectShakeUseCaseImpl.initialize()
        detectRotationUseCase.initialize()

        startShakeEvent()
        startLocationEvent()
        startRotationEvent()

    }

    private fun startRotationEvent() {
        launch {
            detectRotationUseCase.giveLifecycle(viewModelScope)
            detectRotationUseCase.getRotationUpdates().debounce(500).collect {
                dataFlow.value = dataFlow.value.copy(rotationData = it)
            }
        }
    }

    override fun initLocation() {
        lastLocationUseCase.initialize()
    }

    private fun startLocationEvent() {
        launch {
            delay(8000)  // let location updates adjust for precise location on launch
            lastLocationUseCase.giveLifecycle(viewModelScope)
            lastLocationUseCase.getLoactionUodates().collect {
                if (it) {
                    dataFlow.value = dataFlow.value.copy(restartVideo = true)
//                    Toast.makeText(context,"Walked 10 mtrs", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    dataFlow.value = dataFlow.value.copy(restartVideo = false)
                }
            }
        }
    }

    private fun startShakeEvent() {
        launch {
            detectShakeUseCaseImpl.giveLifecycle(viewModelScope)
            detectShakeUseCaseImpl.getShakeUpdates().debounce(1000).collect {
                if (it) {
                    dataFlow.value = dataFlow.value.copy(valPauseVideo = true)
                    delay(500)
                    dataFlow.value = dataFlow.value.copy(valPauseVideo = false)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        detectShakeUseCaseImpl.onPause()
    }

    override fun onResume() {
        super.onResume()
        detectShakeUseCaseImpl.onResume()

    }
}