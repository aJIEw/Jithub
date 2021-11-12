package me.ajiew.jithub.data.service.interceptor

import me.ajiew.jithub.data.model.UserProfile
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 12:16
 */
class AuthHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = UserProfile.accessToken
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }
}

sealed class AuthenticationType {
    object Basic : AuthenticationType()
    object AccessToken : AuthenticationType()
    object None : AuthenticationType()
}