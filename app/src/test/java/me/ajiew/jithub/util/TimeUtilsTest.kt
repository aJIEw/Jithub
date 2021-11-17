package me.ajiew.jithub.util

import org.junit.Test

/**
 *
 * @author aJIEw
 * Created on: 2021/11/15 10:00
 */
class TimeUtilsTest {

    @Test
    fun friendlyTimeDisplay_isCorrect() {
        val time = "2021-11-12T23:02:30Z"
        val result = getFriendlyTime(time)
        println(result)
    }


}