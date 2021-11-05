package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 16:56
 */
data class FeedsTemplate(
    val current_user_public_url: String?,
    val security_advisories_url: String,
    val timeline_url: String,
    val user_url: String
)