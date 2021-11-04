package me.ajiew.core.base.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber


abstract class BaseExpiredInterceptor : BaseInterceptor() {

    override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request? {
        // 不处理
        return null
    }

    override fun onAfterRequest(
        response: Response?,
        chain: Interceptor.Chain,
        bodyString: String
    ): Response? {
        val isExpired = isResponseExpired(response, bodyString)
        if (isExpired) {
            val tmp = responseExpired(response, chain, bodyString)
            if (tmp != null) {
                return tmp
            }
        }
        return response
    }


    /**
     * 判断是否是失效的响应
     *
     * @param oldResponse
     * @param bodyString
     * @return `true` : 失效 <br></br>  `false` : 有效
     */
    abstract fun isResponseExpired(
        oldResponse: Response?,
        bodyString: String?
    ): Boolean

    /**
     * 失效响应的处理
     *
     * @return 获取新的有效请求响应
     */
    abstract fun responseExpired(
        oldResponse: Response?,
        chain: Interceptor.Chain,
        bodyString: String
    ): Response?
}