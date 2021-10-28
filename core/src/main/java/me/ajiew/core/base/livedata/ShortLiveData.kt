package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class ShortLiveData(value : Short = 0) : MutableLiveData<Short>(value) {
    override fun getValue(): Short {
        return super.getValue()!!
    }
}