package com.example.overplaytask.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import com.example.overplaytask.base.di.scopes.ActivityScope
import com.example.overplaytask.exts.hideKeyboard
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class Router @Inject constructor() {

    private var activity: AppCompatActivity? = null

    @IdRes
    private var navHostId: Int? = null

    fun attach(activity: AppCompatActivity, @IdRes navHostId: Int) {
        this.activity = activity
        this.navHostId = navHostId
    }

    fun navigateUp() {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        activity.hideKeyboard()
        val navController = activity.findNavController(navHostId)
        navController.navigateUp()
    }

    fun <T> navigateUp(key: String, data: T?) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        activity.hideKeyboard()
        val navController = activity.findNavController(navHostId)
        navController.previousBackStackEntry?.savedStateHandle?.set(key, data)
        navController.popBackStack()
    }

    fun <T> navigateForResult(
        key: String,
        @IdRes resId: Int,
        args: Bundle? = null,
        callback: (T?) -> Unit
    ) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        try {
            activity.hideKeyboard()
            val navController = activity.findNavController(navHostId)
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            savedStateHandle?.remove<T>(key)
            val observer = Observer<T> {
                Handler().post { // TODO handle without post
                    callback(it)
                }
                savedStateHandle?.remove<T>(key)
            }
            savedStateHandle?.getLiveData<T>(key)?.observeForever(observer)
            navController.navigate(resId, args, getDefaultOptions())
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun <T> navigateForResult(key: String, navDirections: NavDirections, navExtras: Navigator.Extras? = null, callback: (T?) -> Unit) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        try {
            activity.hideKeyboard()
            val navController = activity.findNavController(navHostId)
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            savedStateHandle?.remove<T>(key)
            val observer = Observer<T> {
                Handler().post { // TODO handle without post
                    callback(it)
                }
                savedStateHandle?.remove<T>(key)
            }
            savedStateHandle?.getLiveData<T>(key)?.observeForever(observer)
            if (navExtras != null) {
                navController.navigate(navDirections, navExtras)
            } else {
                navController.navigate(navDirections, getDefaultOptions())
            }
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    private fun getDefaultOptions(): NavOptions? {
//        return NavOptions.Builder()
//            .setEnterAnim(R.anim.fade_in)
//            .setExitAnim(R.anim.fade_out)
//            .setPopEnterAnim(R.anim.fade_in)
//            .setPopExitAnim(R.anim.fade_out)
//            .build()

        return null
    }

    fun navigate(@IdRes resId: Int, args: Bundle? = null) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        try {
            activity.hideKeyboard()
            val navController = activity.findNavController(navHostId)
            navController.navigate(resId, args, getDefaultOptions())
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun navigate(navDirections: NavDirections) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        try {
            activity.hideKeyboard()
            val navController = activity.findNavController(navHostId)
            navController.navigate(navDirections, getDefaultOptions())
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun navigate(navDirections: NavDirections, navExtras: Navigator.Extras) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return

        try {
            activity.hideKeyboard()
            val navController = activity.findNavController(navHostId)
            navController.navigate(navDirections, navExtras)
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }
}
