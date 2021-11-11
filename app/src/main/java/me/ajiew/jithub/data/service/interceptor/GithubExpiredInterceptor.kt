package me.ajiew.jithub.data.service.interceptor

import me.ajiew.core.base.http.BaseExpiredInterceptor
import me.ajiew.core.util.SPUtils
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.data.model.UserProfile
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
        if (oldResponse?.code == CODE_TOKEN_EXCEPTION) {

            UserProfile.clearAll()
            SPUtils.instance.put(Constants.SP_USER_LOGGED_IN, false)
            // TODO: 2021/11/10 show login expired message and login page

            return oldResponse.newBuilder().body(
                bodyString.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
            ).build()
        }

        return null
    }

    companion object {
        const val CODE_TOKEN_EXCEPTION = 401
    }
}