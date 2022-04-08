package com.example.overplaytask.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.Navigation.findNavController
import com.example.overplaytask.R
import com.example.overplaytask.base.components.BaseActivity
import com.example.overplaytask.base.di.activity.ActivityComponent
import com.example.overplaytask.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val bindingInflater = { layoutInflater: LayoutInflater ->
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun getNavHostId() = R.id.globalNavFragment


    override fun injectWith(component: ActivityComponent) {
        component.inject(this)
    }
    override fun onSupportNavigateUp() =
        findNavController(this, R.id.globalNavFragment).navigateUp()
}