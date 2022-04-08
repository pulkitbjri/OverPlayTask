package com.example.overplaytask.base.di.fragment

import androidx.lifecycle.ViewModelProvider
import com.example.overplaytask.base.di.qualifiers.FragmentViewModelFactory
import com.example.overplaytask.utils.MyViewModelFactory
import com.natife.eezy.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
object FragmentModule {

    @Provides
    @FragmentScope
    @FragmentViewModelFactory
    fun viewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory = factory

//
//    @Provides
//    @FragmentScope
//    fun experienceInfoFragment(fragment: Fragment) =
//        ExperienceInfoFragmentArgs.fromBundle(fragment.requireArguments())
}
