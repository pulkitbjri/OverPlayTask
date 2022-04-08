package com.example.overplaytask.base.di.activity

import androidx.lifecycle.ViewModel
import com.example.overplaytask.base.di.ViewModelKey
import com.example.overplaytask.ui.main.MainActivityViewModel
import com.example.overplaytask.ui.main.MainActivityViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainViewModel(viewModel: MainActivityViewModelImpl): ViewModel

}
