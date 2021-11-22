package me.ajiew.jithub.data.service

import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.FeedsTemplate
import me.ajiew.jithub.data.response.User
import me.ajiew.jithub.data.response.UserRepo
import retrofit2.Response
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

    @GET("/users/{name}/events")
    suspend fun getUserEvent(
        @Path("name") name: String,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int
    ): List<EventTimeline>

    /**
     * Here we are using raw response as return type,
     * so we can access response status code:
     *
     * 204: Starred
     * 404: Not starred
     *
     * For more information, please read the
     * [Github doc](https://docs.github.com/en/rest/reference/activity#check-if-a-repository-is-starred-by-the-authenticated-user)
     * */
    @GET("/user/starred/{owner}/{repo}")
    suspend fun checkUserStarredRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Any>

    @PUT("/user/starred/{owner}/{repo}")
    suspend fun starRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Any>

    @DELETE("/user/starred/{owner}/{repo}")
    suspend fun unstarRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Any>

    companion object {
        /**
         * User Timeline results, items per page.
         * @see [getUserTimeline]
         * */
        const val RESULTS_PER_PAGE = 30
    }
}