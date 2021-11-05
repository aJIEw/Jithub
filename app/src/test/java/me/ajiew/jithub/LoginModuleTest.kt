package me.ajiew.jithub

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoginModuleTest {
    @Test
    fun getQueryParams_isCorrect() {
        val token = "access_token=123&scope=gist%2Cnotifications%2Crepo%2Cuser&token_type=bearer"
        val uri = Uri.parse("https://android.com/test?$token")
        assertEquals(uri.getQueryParameter("access_token"), "123")
    }
}