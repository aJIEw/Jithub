package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value) {
    override fun getValue(): Int {
        return super.getValue()!!
    }
}