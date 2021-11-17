package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/11 17:24
 */
data class UserRepo(
    val id: Int,
    val description: String?,
    val forks_count: Int,
    val full_name: String,
    val has_downloads: Boolean,
    val has_issues: Boolean,
    val has_pages: Boolean,
    val has_projects: Boolean,
    val has_wiki: Boolean,
    val homepage: String,
    val language: String?,
    val license: License?,
    val name: String,
    val open_issues_count: Int,
    val organization: Owner,
    val owner: Owner,
    val `private`: Boolean,
    val `public`: Boolean,
    val stargazers_count: Int,
    val url: String,
    val visibility: String,
    val watchers_count: Int
)

data class License(
    val key: String,
    val name: String,
    val url: String
)

data class Owner(
    val id: Int,
    val login: String,
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val html_url: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val starred_url: String,
    val type: String,
    val url: String
)