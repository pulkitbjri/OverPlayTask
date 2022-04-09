package com.example.overplaytask.ui.main

import android.content.Context
import android.widget.Toast
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.useCases.DetectShakeUseCaseImpl
import com.example.overplaytask.useCases.LastLocationUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

abstract class MainActivityViewModel: BaseViewModel() {
}

class MainActivityViewModelImpl @Inject constructor(
    val lastLocationUseCase: LastLocationUseCase,
    val context: Context,
    val detectShakeUseCaseImpl: DetectShakeUseCaseImpl
): MainActivityViewModel() {




}