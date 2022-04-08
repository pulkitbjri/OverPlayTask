package com.example.overplaytask.base.di.fragment

import androidx.fragment.app.Fragment
import com.example.overplaytask.ui.frags.MainFragment
import com.natife.eezy.di.scope.FragmentScope
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        FragmentModule::class,
        ViewModelModule::class,
    ]
)
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): FragmentComponent
    }

    fun inject(mainFragment: MainFragment)

}
