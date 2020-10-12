package com.mouble.baselibrary.base

import androidx.lifecycle.*
import kotlinx.coroutines.*


open class BaseViewModel(): ViewModel(), LifecycleObserver {

    val loadState = MutableLiveData<String>()
    val requestLiveData = MutableLiveData<String>()

    // 创建一个 根协程 Dispatchers.Main 在主线程中
    val presenterScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        presenterScope.cancel()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun ON_CREATE() {
        println("@@@@@@@@MyObserver:ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun ON_START() {
        println("@@@@@@@@MyObserver:ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun ON_RESUME() {
        println("@@@@@@@@MyObserver:ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun ON_PAUSE() {
        println("@@@@@@@@MyObserver:ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun ON_STOP() {
        println("@@@@@@@@MyObserver:ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun ON_DESTROY() {
        println("@@@@@@@@MyObserver:ON_DESTROY")
    }
}