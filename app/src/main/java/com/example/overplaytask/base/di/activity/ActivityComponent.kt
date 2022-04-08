package com.example.overplaytask.base.di.activity

import com.example.overplaytask.base.components.BaseActivity
import com.example.overplaytask.ui.MainActivity
import com.example.overplaytask.base.di.FragmentComponent
import com.example.overplaytask.base.di.scopes.ActivityScope
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ActivitySubComponents::class
    ]
)
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: BaseActivity<*, *>
        ): ActivityComponent
    }

    fun fragmentComponent(): FragmentComponent.Factory

    fun inject(activity: MainActivity)

}
