package com.example.overplaytask.ui.frags

import android.content.Context
import android.widget.Toast
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.useCases.LastLocationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val context: Context
): MainFragmentViewModel(){
    override val dataFlow = MutableStateFlow(ViewData())

    init {
        launch {
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



}