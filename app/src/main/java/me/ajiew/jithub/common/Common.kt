package me.ajiew.jithub.common

import me.ajiew.jithub.BuildConfig
import me.ajiew.jithub.BuildConfig.GITHUB_CLIENT_ID


object Constants {

    const val SP_EULA_PASS = "SP_EULA_PASS"
    const val SP_USER_LOGGED_IN = "SP_USER_LOGGED_IN"

    const val GITHUB_OAUTH_REDIRECT_URL = "jithub://oauth.login"
    const val GITHUB_OAUTH_AUTHORIZE_URL = "https://github.com/login/oauth/authorize" +
            "?client_id=$GITHUB_CLIENT_ID" +
            "&state=${BuildConfig.APPLICATION_ID}" +
            "&redirect_uri=jithub://oauth.login" +
            "&scope=repo%20gist%20notifications%20user%20" // repo gits notifications user
}