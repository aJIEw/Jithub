package me.ajiew.jithub.data.model

import timber.log.Timber

object UserProfile {

    var userName: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                Timber.d("Welcome, $value")
            }
            field = value
        }

    var accessToken: String = ""

    fun clearAll() {
        userName = ""
        accessToken = ""
    }
}