package me.ajiew.jithub.util

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 *
 * @author aJIEw
 * Created on: 2021/11/15 10:30
 */
fun getFriendlyTime(time: String): String {
    val result = StringBuilder()
    val parsed = Instant.parse(time).atZone(ZoneOffset.systemDefault())
    val millis = parsed.toEpochSecond() * 1000
    val spanMinutes = TimeUtils.getTimeSpanByNow(millis, TimeConstants.MIN) * -1
    if (spanMinutes > 59) {
        val spanHour = TimeUtils.getTimeSpanByNow(millis, TimeConstants.HOUR) * -1
        if (spanHour > 23) {
            var spanDay = TimeUtils.getTimeSpanByNow(millis, TimeConstants.DAY) * -1
            val nowTime = Instant.now().atZone(ZoneOffset.systemDefault())
            if (spanDay <= 1 && nowTime.dayOfMonth.minus(1) == parsed.dayOfMonth) {
                result.append("yesterday")
            } else {
                if ((spanHour - nowTime.hour).toDouble() / 24 > spanDay) {
                    spanDay += 1
                }
                result.autoPluralWords("day", spanDay.toInt())

                if (spanDay > 30) {
                    result.clear()
                    result.append(parsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                }
            }
        } else {
            result.autoPluralWords("hour", spanHour.toInt())
        }
    } else {
        result.autoPluralWords("minute", spanMinutes.toInt())
    }

    return result.toString()
}

private fun StringBuilder.autoPluralWords(keywords: String, param: Int) {
    append(param)
        .append(if (param > 1) " ${keywords}s" else " $keywords")
        .append(" ago")
}
