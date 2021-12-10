package me.ajiew.jithub.data.service.interceptor

import me.ajiew.jithub.data.model.UserProfile
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 12:16
 */
class AuthHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val accessToken = UserProfile.accessToken
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            return chain.proceed(request)
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
        }

        return Response.Builder().build()
    }
}

sealed class AuthenticationType {
    object Basic : AuthenticationType()
    object AccessToken : AuthenticationType()
    object None : AuthenticationType()
}