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
    val organization: User?,
    val owner: User,
    val `private`: Boolean?,
    val `public`: Boolean?,
    val stargazers_count: Int,
    val url: String, // api url
    val html_url: String?, // repo page url
    val visibility: String,
    val watchers_count: Int,
    val created_at: String?,
    val updated_at: String?,
)

data class License(
    val key: String,
    val name: String,
    val url: String
)