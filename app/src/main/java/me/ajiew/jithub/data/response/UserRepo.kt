package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/11 17:24
 */
data class UserRepo(
    val allow_forking: Boolean,
    val archive_url: String,
    val archived: Boolean,
    val branches_url: String,
    val clone_url: String,
    val collaborators_url: String,
    val comments_url: String,
    val commits_url: String,
    val contents_url: String,
    val contributors_url: String,
    val created_at: String,
    val description: String,
    val downloads_url: String,
    val events_url: String,
    val fork: Boolean,
    val forks: Int,
    val forks_count: Int,
    val forks_url: String,
    val full_name: String,
    val git_commits_url: String,
    val git_url: String,
    val has_downloads: Boolean,
    val has_issues: Boolean,
    val has_pages: Boolean,
    val has_projects: Boolean,
    val has_wiki: Boolean,
    val homepage: String,
    val id: Int,
    val issues_url: String,
    val language: String?,
    val license: License,
    val name: String,
    val notifications_url: String,
    val open_issues: Int,
    val open_issues_count: Int,
    val organization: Owner,
    val owner: Owner,
    val `private`: Boolean,
    val `public`: Boolean,
    val pulls_url: String,
    val releases_url: String,
    val stargazers_count: Int,
    val stargazers_url: String,
    val tags_url: String,
    val url: String,
    val visibility: String,
    val watchers: Int,
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