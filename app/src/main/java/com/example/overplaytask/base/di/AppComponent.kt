package com.example.overplaytask.base.di

import com.example.overplaytask.App
import com.example.overplaytask.base.di.activity.ActivityComponent
import com.example.overplaytask.base.di.activity.ActivityViewModelModule
import com.example.overplaytask.base.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppSubComponentsModule::class,
        ActivityViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: App): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory

    fun inject(app: App)

//    fun inject(messagingService: FirebaseService)
//
//    fun inject(glideModule: EezyGlideModule)
}
