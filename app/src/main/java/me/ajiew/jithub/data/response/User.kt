package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/17 10:18
 */
data class User(
    val id: Int,
    val avatar_url: String?,
    val bio: String?,
    val blog: String?,
    val collaborators: Int?,
    val company: String?,
    val created_at: String?,
    val disk_usage: Int?,
    val email: String?,
    val events_url: String?,
    val followers: Int?,
    val followers_url: String?,
    val following: Int?,
    val following_url: String?,
    val gists_url: String?,
    val gravatar_id: String?,
    val html_url: String?,
    val location: String?,
    val login: String?,
    val name: String?,
    val organizations_url: String?,
    val private_gists: Int?,
    val public_gists: Int?,
    val public_repos: Int?,
    val received_events_url: String?,
    val repos_url: String?,
    val starred_url: String?,
    val subscriptions_url: String?,
    val total_private_repos: Int?,
    val twitter_username: String?,
    val type: String?,
    val url: String?
)