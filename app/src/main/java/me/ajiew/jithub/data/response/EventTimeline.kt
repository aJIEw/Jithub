package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 15:46
 */
data class EventTimeline(
    val actor: Actor,
    val created_at: String,
    val id: String,
    val payload: Payload,
    val `public`: Boolean,
    val repo: Repo,
    val type: String
)

data class Actor(
    val avatar_url: String,
    val display_login: String,
    val id: Int,
    val login: String,
    val url: String
)

data class Payload(
    val action: String?,
    val forkee: UserRepo?,
    val ref_type: String?,
    val release: ReleaseRepo?,
)

data class Repo(
    val id: Int,
    val name: String,
    val url: String
)

data class ReleaseRepo(
    val author: Owner,
    val body: String,
    val id: Int,
    val name: String,
    val tag_name: String,
    val url: String,
)