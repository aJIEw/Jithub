package me.ajiew.core.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        throw UnsupportedOperationException(
            "create(Class<?>) must be called on implementaions of BaseViewModelFactory"
        )
    }
}