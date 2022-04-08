package com.example.overplaytask.base.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityViewModelFactory

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentViewModelFactory
