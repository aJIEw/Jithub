package me.ajiew.jithub.data.model

/**
 * GitHub event types:
 * @link https://docs.github.com/en/developers/webhooks-and-events/events/github-event-types
 * */
sealed class GithubEvent(val type: String) {
    object WatchEvent : GithubEvent("WatchEvent")
    object ForkEvent : GithubEvent("ForkEvent")
    object ReleaseEvent : GithubEvent("ReleaseEvent")
    object CreateEvent : GithubEvent("CreateEvent")
    object PushEvent : GithubEvent("PushEvent")
    object PublicEvent : GithubEvent("PublicEvent")
    object IssuesEvent : GithubEvent("IssuesEvent")
    object IssueCommentEvent : GithubEvent("IssueCommentEvent")
}
