package com.example.overplaytask.base.di.modules

import com.example.overplaytask.useCases.CalculateDistanceUseCase
import com.example.overplaytask.useCases.CalculateDistanceUseCaseImpl
import com.example.overplaytask.useCases.LastLocationUseCase
import com.example.overplaytask.useCases.LastLocationUseCaseImpl
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
}