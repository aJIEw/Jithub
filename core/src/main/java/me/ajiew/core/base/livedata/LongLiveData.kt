package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class LongLiveData(value: Long = 0L) : MutableLiveData<Long>(value) {
    override fun getValue(): Long {
        return super.getValue()!!
    }
}