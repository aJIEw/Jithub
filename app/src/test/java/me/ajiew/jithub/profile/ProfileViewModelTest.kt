package me.ajiew.jithub.profile

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 *
 * @author aJIEw
 * Created on: 2021/11/18 14:12
 */
class ProfileViewModelTest {

    private val today: ZonedDateTime =
        Instant.parse("2021-11-18T10:29:23Z").atZone(ZoneId.systemDefault())

    @Test
    fun dateParseFormat_isCorrect() {
        val date =
            Instant.parse("2021-11-12T10:29:23Z").atZone(ZoneId.systemDefault()).toLocalDateTime()
        val truncated = date.truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ")
        println(
            """
            |parsed:     $truncated""".trimMargin()
        )

        val formatted = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        println(
            """
            |formatted:  $formatted""".trimMargin()
        )
    }

    @Test
    fun calculateDateDuration_isCorrect() {
        println("""today:   ${today.toLocalDateTime()}""")

        val date0 =
            Instant.parse("2021-11-17T10:29:23Z").atZone(ZoneId.systemDefault()).toLocalDate()
        val daysBetween0 =
            Duration.between(date0.atStartOfDay(), today.toLocalDate().atStartOfDay()).toDays()
        assertEquals(1, daysBetween0)

        val date1 =
            Instant.parse("2021-08-19T10:29:23Z").atZone(ZoneId.systemDefault()).toLocalDate()
        val daysBetween1 =
            Duration.between(date1.atStartOfDay(), today.toLocalDate().atStartOfDay()).toDays()
        assertEquals(91, daysBetween1)
    }
}