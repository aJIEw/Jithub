package me.ajiew.jithub.data.service

import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.FeedsTemplate
import me.ajiew.jithub.data.response.User
import me.ajiew.jithub.data.response.UserRepo
import retrofit2.http.*

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 10:50
 */
interface UserService {

    @GET("feeds")
    suspend fun getUserFeeds(): FeedsTemplate

    @GET("users/{name}")
    suspend fun getUserInfo(@Path("name") name: String): User

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{name}/received_events")
    suspend fun getUserTimeline(
        @Path("name") userName: String,
        @Query("page") page: Int = 1,
    ): List<EventTimeline>

    @GET
    suspend fun getUserRepo(@Url url: String): UserRepo

    companion object {
        /**
         * User Timeline results, items per page.
         * @see [getUserTimeline]
         * */
        const val RESULTS_PER_PAGE = 30
    }
}