package me.ajiew.core.base.http

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

abstract class BaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = onBeforeRequest(chain.request(), chain) ?: chain.request()
        val response: Response = chain.proceed(request)
        val isText: Boolean = isText(response.body?.contentType())
        if (!isText) {
            return response
        }
        return onAfterRequest(response, chain, getResponseBodyString(response)) ?: response
    }

    /**
     * 请求拦截
     *
     * @param request
     * @param chain
     * @return `null` : 不进行拦截处理
     */
    abstract fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request?

    /**
     * 响应拦截
     *
     * @param response
     * @param chain
     * @param bodyString
     * @return `null` : 不进行拦截处理
     */
    abstract fun onAfterRequest(
        response: Response?,
        chain: Interceptor.Chain,
        bodyString: String
    ): Response?

    private fun isText(mediaType: MediaType?): Boolean {
        return mediaType != null && (mediaType.type == "text" || mediaType.subtype == "json")
    }

    private fun getResponseBodyString(response: Response?): String {
        val responseBody = response?.body
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer

        var charset: Charset? = null
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(StandardCharsets.UTF_8)
        }
        return buffer.clone().readString(charset ?: StandardCharsets.UTF_8)
    }
}