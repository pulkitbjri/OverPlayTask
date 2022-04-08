package com.example.overplaytask.base.di

import com.example.overplaytask.base.di.activity.ActivityComponent
import dagger.Module

@Module(
    subcomponents = [
        ActivityComponent::class
    ]
)
object AppSubComponentsModule
