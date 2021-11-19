package me.ajiew.jithub.data.response

/**
 * Represent a GitHub Event
 *
 * @link https://docs.github.com/en/developers/webhooks-and-events/events/github-event-types
 */
data class EventTimeline(
    val actor: Actor,
    val created_at: String,
    val id: String,
    val payload: Payload,
    val `public`: Boolean,
    val repo: Repo,
    val type: String,
)

data class Actor(
    val avatar_url: String,
    val display_login: String,
    val id: Int,
    val login: String,
    val url: String
)

data class Payload(
    val action: String?, // watch event
    val forkee: UserRepo?, // fork event
    val ref_type: String?, // create event
    val release: ReleaseRepo?, // release event
    val commits: List<Commit>?, // push event
    val size: Int? // commits number
)

data class Commit(
    val author: Author,
    val distinct: Boolean,
    val message: String,
    val sha: String,
    val url: String
)

data class Author(
    val email: String,
    val name: String
)

data class Repo(
    val id: Int,
    val name: String,
    val url: String
)

data class ReleaseRepo(
    val author: User,
    val body: String,
    val id: Int,
    val name: String,
    val tag_name: String,
    val url: String,
)