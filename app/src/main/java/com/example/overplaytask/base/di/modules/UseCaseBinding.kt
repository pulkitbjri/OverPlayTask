package com.example.overplaytask.base.di.modules

import com.example.overplaytask.useCases.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UseCaseBinding {
    @Binds
    @Singleton
    fun lastLocationUseCase(useCase: LastLocationUseCaseImpl): LastLocationUseCase

    @Binds
    fun calculateDistanceUseCase(useCase: CalculateDistanceUseCaseImpl): CalculateDistanceUseCase

    @Binds
    @Singleton
    fun detectShakeUseCase(useCase: DetectShakeUseCaseImpl): DetectShakeUseCase
}
