package com.example.overplaytask.base.components

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.viewbinding.ViewBinding
import com.example.overplaytask.base.di.ComponentProvider
import com.example.overplaytask.base.di.activity.ActivityComponent
import com.example.overplaytask.base.di.qualifiers.ActivityViewModelFactory
import com.example.overplaytask.utils.Router
import com.example.overplaytask.utils.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    lateinit var component: ActivityComponent

    @Inject
    lateinit var router: Router

    @Inject
    @ActivityViewModelFactory
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: VM

    protected val defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Activity exception handler")
        onError(throwable)
    }

    protected open fun onError(throwable: Throwable) {
        toast(throwable.localizedMessage)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
            component = (application as ComponentProvider).provideAppComponent()
                .activityComponent()
                .create(this)
            injectWith(component)
            router.attach(this, getNavHostId())

        viewModel = ViewModelProvider(this, factory).get(getViewModelKClass().java)

        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(LayoutInflater.from(this))
        setContentView(_binding!!.root)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        return actualClass.kotlin
    }

    override fun onDestroy() {
        super.onDestroy()
        router.detach()
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return lifecycleScope.launch(defaultErrorHandler) {
            this.block()
        }
    }

    protected fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job = lifecycleScope.launch(defaultErrorHandler) {
        lifecycle.whenStarted(block)
    }

    var _binding: VB? = null

    abstract val bindingInflater: (LayoutInflater) -> VB

    @IdRes
    abstract fun getNavHostId(): Int

    abstract fun injectWith(component: ActivityComponent)
}
