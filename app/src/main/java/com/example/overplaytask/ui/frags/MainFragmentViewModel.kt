package com.example.overplaytask.ui.frags

import android.content.Context
import android.widget.Toast
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.useCases.DetectShakeUseCaseImpl
import com.example.overplaytask.useCases.LastLocationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

abstract class MainFragmentViewModel : BaseViewModel() {
    data class ViewData(
        val restartVideo : Boolean = false,
        val valPauseVideo : Boolean = false,
    )
    abstract val dataFlow: StateFlow<ViewData>

}

class MainFragmentViewModelImpl  @Inject constructor(
    val lastLocationUseCase : LastLocationUseCase,
    val detectShakeUseCaseImpl: DetectShakeUseCaseImpl,
    val context: Context
): MainFragmentViewModel(){
    override val dataFlow = MutableStateFlow(ViewData())

    init {
        startShakeEvent()
        startLocationEvent()

    }

    private fun startLocationEvent() {
        launch {
            delay(8000)  // let location updates adjust for precise location on launch
            lastLocationUseCase.getLoactionUodates().collect{
                if (it){
                    dataFlow.value = dataFlow.value.copy(restartVideo = true)
                    Toast.makeText(context,"Walked 10 mtrs", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    dataFlow.value = dataFlow.value.copy(restartVideo = false)

                }

            }
        }
    }

    private fun startShakeEvent() {
        launch {
            detectShakeUseCaseImpl.getShakeUpdates().collectLatest{
                if (it){
                    Toast.makeText(context, "Shake event detected", Toast.LENGTH_SHORT).show()
                    dataFlow.value = dataFlow.value.copy(valPauseVideo = true)
                    delay(500)
                    dataFlow.value = dataFlow.value.copy(valPauseVideo = false)
                }
            }
        }
    }


}