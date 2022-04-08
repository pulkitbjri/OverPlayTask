package com.example.overplaytask.base.di.activity

import com.example.overplaytask.base.di.fragment.FragmentComponent
import dagger.Module

@Module(
    subcomponents = [
        FragmentComponent::class
    ]
)
object ActivitySubComponents
