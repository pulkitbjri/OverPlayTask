package com.example.overplaytask.ui.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overplaytask.R
import com.example.overplaytask.base.components.BaseFragment
import com.example.overplaytask.base.components.BaseViewModel
import com.example.overplaytask.base.di.fragment.FragmentComponent
import com.example.overplaytask.databinding.FragmentMianBinding

class MainFragment : BaseFragment<FragmentMianBinding, MainFragmentViewModel>() {



    override val bindingInflater = { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, b: Boolean ->
        FragmentMianBinding.inflate(layoutInflater, viewGroup, b)
    }

    override fun injectWith(component: FragmentComponent) = component.inject(this)



}