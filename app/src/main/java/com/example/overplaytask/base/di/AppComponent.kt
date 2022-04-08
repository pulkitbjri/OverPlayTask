package com.example.overplaytask.base.di

import com.example.overplaytask.Application
import com.example.overplaytask.base.di.activity.ActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
//        WorkerModule::class,
//        AppModule::class,
//        AmplifyDownloaderModule::class,
//        DBModule::class,
//        ApiInterceptorModule::class,
//        ApiModule::class,
//        RetrofitModule::class,
//        PrefsModule::class,
//        PrefsBinding::class,
//        ValidatorModule::class,
//        AppSubComponentsModule::class,
//        UseCaseBinding::class,
//        SecondUseCaseBinding::class,
//        EventBinding::class,
//        AnalyticsBinding::class,
//        NetworkDataSourceBinding::class,
//        CacheDataSourceBinding::class,
//        MainViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory

    fun inject(application: Application)

//    fun inject(messagingService: FirebaseService)
//
//    fun inject(glideModule: EezyGlideModule)
}
