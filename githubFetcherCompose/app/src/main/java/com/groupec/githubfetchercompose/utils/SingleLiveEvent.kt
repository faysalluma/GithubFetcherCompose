package com.groupec.famocopayapp2apptoolscompose.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> constructor(value: T) : MediatorLiveData<T>() {
    companion object {
        private val TAG = SingleLiveEvent::class.java.simpleName

        @MainThread
        fun <X, Y> mapSingleLiveEvent(
            source: LiveData<X>,
            mapFunction: Function<X?, Y?>
        ): SingleLiveEvent<Y?> {
            val result = SingleLiveEvent<Y?>(null)
            result.addSource(
                source
            ) { x -> result.setValue(mapFunction.apply(x)) }
            return result
        }
    }

    private val pending = AtomicBoolean(false)

    init {
        postValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}