package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


class StringLiveData(value: String = "") : MutableLiveData<String>(value) {
    override fun getValue(): String {
        return super.getValue()!!
    }
}