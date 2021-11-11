package me.ajiew.jithub.data.model

object UserProfile {

    var userName: String = ""

    var accessToken: String = ""

    fun clearAll() {
        userName = ""
        accessToken = ""
    }
}