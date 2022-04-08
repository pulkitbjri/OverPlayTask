package com.example.overplaytask.base.di.fragment

import androidx.lifecycle.ViewModel
import com.example.overplaytask.base.di.ViewModelKey
import com.example.overplaytask.ui.frags.MainFragmentViewModel
import com.example.overplaytask.ui.frags.MainFragmentViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    fun moodSelectionViewModel(viewModel: MainFragmentViewModelImpl): ViewModel
}
