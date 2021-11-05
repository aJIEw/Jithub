package me.ajiew.jithub.data.service

import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.FeedsTemplate
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 10:50
 */
interface UserService {

    // TODO: 2021/11/5 Add access_token
    @GET("feeds")
    suspend fun getUserFeeds(): FeedsTemplate

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{name}/received_events")
    suspend fun getUserTimeline(@Path("name") userName: String): List<EventTimeline>
}