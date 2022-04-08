package com.example.overplaytask.base.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.example.overplaytask.base.di.fragment.FragmentComponent
import com.example.overplaytask.base.di.qualifiers.FragmentViewModelFactory
import com.example.overplaytask.databinding.FragmentBaseBinding
import com.example.overplaytask.utils.Router
import com.example.overplaytask.utils.toast
import com.example.overplaytask.exts.subscribe
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private val defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
        Timber.e(throwable, "Fragment exception handler")
    }

    private var _binding: VB? = null

    protected val binding get() = requireNotNull(_binding)

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Inject
    @FragmentViewModelFactory
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: VM

    lateinit var component: FragmentComponent

    @Inject
    lateinit var router: Router

    protected var toolbar: Toolbar? = null

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch(defaultErrorHandler) {
            this.block()
        }
    }

    protected fun launchSilent(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch(defaultErrorHandler) {
            this.block()
        }
    }

    protected fun launchForFlow(block: suspend CoroutineScope.() -> Unit): Job = launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this.block()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val baseActivity = context as BaseActivity<*, *>


            component = baseActivity.component
                .fragmentComponent()
                .create(this)
            injectWith(component)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = FragmentBaseBinding.inflate(layoutInflater)
        toolbar = root.toolbar
        toolbar?.isVisible = false
        _binding = bindingInflater.invoke(inflater, container, false)
        root.root.addView(binding.root, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        viewModel = ViewModelProvider(this, factory).get(getViewModelKClass().java)
        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        subscribe(viewModel.defaultErrorLiveData) {
            onError(it)
        }

        return root.root
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        return actualClass.kotlin
    }

    abstract fun injectWith(component: FragmentComponent)

    protected open fun onError(throwable: Throwable) {
        try {
            toast(throwable.localizedMessage)
        } catch (ignored: Exception) {}
    }


    override fun onDestroyView() {
        toolbar = null
        _binding = null
        super.onDestroyView()
    }


}
