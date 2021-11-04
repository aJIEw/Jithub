package me.ajiew.jithub.util

import java.util.*

/**
 *
 * @author aJIEw
 * Created on: 2021/11/3 15:54
 */
fun getRandomString(length: Int): String {
    val base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMOPQRSTUVWXYZ0123456789"
    val random = Random()
    val sb = StringBuilder()
    for (i in 0 until length) {
        val number = random.nextInt(base.length)
        sb.append(base[number])
    }
    return sb.toString()
}