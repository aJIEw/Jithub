package me.ajiew.core.base.livedata

import androidx.lifecycle.MutableLiveData


/**
 * Boolean 类型的 MutableLiveData，提供了默认值，避免取值的时候判空或者强制非空转换
 */
class BooleanLiveData(value: Boolean = false) : MutableLiveData<Boolean>(value) {
    override fun getValue(): Boolean {
        return super.getValue()!!
    }
}

