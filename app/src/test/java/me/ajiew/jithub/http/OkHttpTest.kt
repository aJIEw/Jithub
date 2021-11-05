package me.ajiew.jithub.http

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 19:57
 */
class OkHttpTest {
    @Test
    fun responseBodyBuilder_isCorrect() {
        val builder = Response.Builder()
        val jsonBody = """{"code": 401, "message": "expired", "data": null}"""
        val body = builder
            .protocol(Protocol.HTTP_2)
            .request(Request.Builder().url("https://ajiew.me").build())
            .code(401)
            .message("Need Authorize")
            .body(jsonBody.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
            ).build()

        assertEquals(jsonBody, body.body?.string())
    }
}