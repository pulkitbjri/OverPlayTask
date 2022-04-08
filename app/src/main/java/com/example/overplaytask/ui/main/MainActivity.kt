package com.example.overplaytask.ui.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.Navigation.findNavController
import com.example.overplaytask.R
import com.example.overplaytask.base.components.BaseActivity
import com.example.overplaytask.base.di.activity.ActivityComponent
import com.example.overplaytask.databinding.ActivityMainBinding
import com.example.overplaytask.exts.PermissionResponseHandler
import com.example.overplaytask.exts.withAllPermissions

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withAllPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            responseHandler = object : PermissionResponseHandler {
                override fun onPermissionGranted() {
//                    lastLocationUseCase.initialize()
                }

                override fun onPermissionRejected() {

                }
            }
        )
    }
}