package com.example.overplaytask.ui.main

import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.useCases.DetectShakeUseCaseImpl
import com.example.overplaytask.useCases.LastLocationUseCase
import javax.inject.Inject

abstract class MainActivityViewModel: BaseViewModel() {
    abstract fun initLocation()
}

class MainActivityViewModelImpl @Inject constructor(
    val lastLocationUseCase: LastLocationUseCase,
    val detectShakeUseCaseImpl: DetectShakeUseCaseImpl
): MainActivityViewModel() {

    init {
        detectShakeUseCaseImpl.initialize()
    }
    override fun initLocation() {
        lastLocationUseCase.initialize()
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