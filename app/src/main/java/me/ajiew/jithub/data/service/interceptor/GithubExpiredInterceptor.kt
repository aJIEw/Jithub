package me.ajiew.jithub.data.service.interceptor

import me.ajiew.core.base.http.BaseExpiredInterceptor
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 18:19
 */
class GithubExpiredInterceptor : BaseExpiredInterceptor() {
    override fun isResponseExpired(oldResponse: Response?, bodyString: String?): Boolean {
        if (oldResponse?.code == CODE_TOKEN_EXCEPTION) {
            return true
        }

        return false
    }

    override fun responseExpired(
        oldResponse: Response?,
        chain: Interceptor.Chain,
        bodyString: String
    ): Response? {
        val responseBuilder = Response.Builder()

        if (oldResponse?.code == CODE_TOKEN_EXCEPTION) {
            return responseBuilder.body(
                bodyString.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
            ).build()
        }

        return null
    }

    companion object {
        const val CODE_TOKEN_EXCEPTION = 401
    }
}