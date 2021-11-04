package me.ajiew.jithub.data.response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/3 15:13
 */
data class AuthToken(
    val access_token: String,
    val scope: String,
    val token_type: String
)