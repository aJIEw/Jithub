package me.ajiew.jithub.data.repository

import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.data.Results
import me.ajiew.jithub.data.response.*
import retrofit2.Response

/**
 *
 * @author aJIEw
 * Created on: 2021/11/2 17:03
 */
interface UserRepository : IRepository {

    suspend fun requestAuthToken(code: String): Results<AuthToken>

    fun getAccessToken(): String

    fun saveAccessToken(token: String)

    suspend fun requestUserFeeds(): Results<FeedsTemplate>

    fun getUserName(): String

    fun saveUserName(name: String)

    suspend fun requestUserInfo(): Results<User>

    suspend fun requestUserTimeline(name: String, page: Int = 1): Results<List<EventTimeline>>

    suspend fun requestUserRepo(url: String): Results<UserRepo>

    suspend fun requestUserEvent(page: Int): Results<List<EventTimeline>>

    suspend fun requestCheckUserStarredRepo(owner: String, repo: String): Response<Any>

    suspend fun requestStarRepo(owner: String, repo: String): Response<Any>

    suspend fun requestUnstarRepo(owner: String, repo: String): Response<Any>
}