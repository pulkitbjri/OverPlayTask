package com.example.overplaytask.base.di.modules

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.eezy.util.ResourceProvider
import com.eezy.util.ResourceProviderImpl
import com.example.overplaytask.App
import com.example.overplaytask.base.di.qualifiers.ActivityViewModelFactory
import com.example.overplaytask.utils.MyViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun context(app: App): Context = app

    @Provides
    @Singleton
    fun resourceProvider(provider: ResourceProviderImpl): ResourceProvider {
        return provider
    }

    @Provides
    @Singleton
    @ActivityViewModelFactory
    fun viewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory = factory
}
