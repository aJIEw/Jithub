package me.ajiew.jithub.util

import com.blankj.utilcode.util.EncryptUtils
import org.junit.Test
import org.junit.Assert.*

/**
 *
 * @author aJIEw
 * Created on: 2021/11/3 15:55
 */
class StringUtilTest {
    @Test
    fun randomString_lengthIsCorrect() {
        assertEquals(0, getRandomString(-1).length)
        assertEquals(0, getRandomString(0).length)
        assertEquals(10, getRandomString(10).length)
    }
}