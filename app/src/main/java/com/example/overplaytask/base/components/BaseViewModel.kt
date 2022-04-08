package com.example.overplaytask.base.components

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.overplaytask.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private val _defaultErrorLiveData = SingleLiveEvent<Throwable>()

    val defaultErrorLiveData: LiveData<Throwable> = _defaultErrorLiveData

//    val networkLiveData: NetworkLiveData = NetworkLiveData

    protected val defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _defaultErrorLiveData.postValue(throwable)
        Timber.e(throwable, "ViewModel exception handler")
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(defaultErrorHandler) {
            this.block()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreateView() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    open fun onDestroyView() {
    }
}
