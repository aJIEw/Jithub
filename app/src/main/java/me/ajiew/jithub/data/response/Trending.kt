package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/10/28 18:02
 */
data class TrendingRepo(
    val author: String,
    val avatar: String,
    val builtBy: List<BuiltBy>,
    val currentPeriodStars: Int,
    val description: String,
    val forks: Int,
    val language: String?,
    val languageColor: String?,
    val name: String,
    val stars: Int,
    val url: String
)

data class BuiltBy(
    val avatar: String,
    val href: String,
    val username: String
)