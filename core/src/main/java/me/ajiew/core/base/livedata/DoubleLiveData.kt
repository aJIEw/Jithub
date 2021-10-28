package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class DoubleLiveData(value: Double = 0.0) : MutableLiveData<Double>(value) {
    override fun getValue(): Double {
        return super.getValue()!!
    }
}