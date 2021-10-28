package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class ByteLiveData(value: Byte = 0) : MutableLiveData<Byte>() {
    override fun getValue(): Byte {
        return super.getValue()!!
    }
}