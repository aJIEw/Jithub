package me.ajiew.jithub.data.repository

import com.blankj.utilcode.util.EncryptUtils
import me.ajiew.core.base.repository.BaseRepositoryBoth
import me.ajiew.core.base.repository.ILocalDataSource
import me.ajiew.core.base.repository.IRemoteDataSource
import me.ajiew.core.data.Results
import me.ajiew.core.util.SPUtils
import me.ajiew.core.util.SingletonHolderDoubleArg
import me.ajiew.jithub.BuildConfig.GITHUB_CLIENT_ID
import me.ajiew.jithub.BuildConfig.GITHUB_CLIENT_SECRET
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.response.*
import me.ajiew.jithub.data.service.LoginService
import me.ajiew.jithub.data.service.UserService
import retrofit2.Response
import timber.log.Timber

/**
 *
 * @author aJIEw
 * Created on: 2021/11/2 17:14
 */
class UserRepositoryImpl(
    remoteDataSource: UserRemoteDataSource,
    localDataSource: UserLocalDataSource
) : UserRepository,
    BaseRepositoryBoth<UserRemoteDataSource, UserLocalDataSource>(
        remoteDataSource,
        localDataSource
    ) {

    override suspend fun requestAuthToken(code: String): Results<AuthToken> {
        return try {
            val result = remoteDataSource.getAuthToken(code)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override fun getAccessToken(): String {
        val token = localDataSource.accessToken
        if (token.isNotEmpty()) {
            UserProfile.accessToken = token
            SPUtils.instance.put(Constants.SP_USER_LOGGED_IN, true)
        }
        return token
    }

    override fun saveAccessToken(token: String) {
        UserProfile.accessToken = token
        SPUtils.instance.put(Constants.SP_USER_LOGGED_IN, true)
        localDataSource.accessToken = token
    }

    override suspend fun requestUserFeeds(): Results<FeedsTemplate> {
        return try {
            val result = remoteDataSource.getUserFeeds()
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override fun getUserName(): String {
        val name = localDataSource.userName
        if (name.isNotEmpty()) {
            UserProfile.userName = name
        }
        return name
    }

    override fun saveUserName(name: String) {
        UserProfile.userName = name
        localDataSource.userName = name
    }

    override suspend fun requestUserInfo(): Results<User> {
        return try {
            val userName = UserProfile.userName
            val result = remoteDataSource.getUserInfo(userName)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override suspend fun requestUserTimeline(name: String, page: Int): Results<List<EventTimeline>> {
        return try {
            val result = remoteDataSource.getUserTimeline(name, page)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override suspend fun requestUserRepo(url: String): Results<UserRepo> {
        return try {
            val result = remoteDataSource.getUserRepoInfo(url)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override suspend fun requestUserEvent(page: Int): Results<List<EventTimeline>> {
        return try {
            val userName = UserProfile.userName
            val result = remoteDataSource.getUserEvent(userName, page)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    override suspend fun requestCheckUserStarredRepo(owner: String, repo: String): Response<Any> {
        return remoteDataSource.checkUserStarredRepo(owner, repo)
    }

    override suspend fun requestStarRepo(owner: String, repo: String): Response<Any> {
        return remoteDataSource.starRepo(owner, repo)
    }

    override suspend fun requestUnstarRepo(owner: String, repo: String): Response<Any> {
        return remoteDataSource.unstarRepo(owner, repo)
    }

    override suspend fun requestUserRepoList(page: Int): Results<List<UserRepo>> {
        return try {
            val result = remoteDataSource.getUserRepoList(page)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    companion object :
        SingletonHolderDoubleArg<UserRepositoryImpl, UserRemoteDataSource, UserLocalDataSource>(::UserRepositoryImpl)
}

class UserRemoteDataSource(
    private val loginService: LoginService,
    private val userService: UserService
) : IRemoteDataSource {

    suspend fun getAuthToken(code: String): AuthToken {
        return loginService.getAuthToken(
            clientId = GITHUB_CLIENT_ID,
            clientSecret = GITHUB_CLIENT_SECRET,
            code = code
        )
    }

    suspend fun getUserInfo(name: String): User {
        return userService.getUserInfo(name)
    }

    suspend fun getUserFeeds(): FeedsTemplate {
        return userService.getUserFeeds()
    }

    suspend fun getUserTimeline(name: String, page: Int): List<EventTimeline> {
        return userService.getUserTimeline(name, page)
    }

    suspend fun getUserRepoInfo(url: String) : UserRepo {
        return userService.getUserRepo(url)
    }

    suspend fun getUserEvent(name: String, page: Int): List<EventTimeline> {
        return userService.getUserEvent(name, page = page)
    }

    suspend fun checkUserStarredRepo(owner: String, repo: String): Response<Any> {
        return userService.checkUserStarredRepo(owner, repo)
    }

    suspend fun starRepo(owner: String, repo: String): Response<Any> {
        return userService.starRepo(owner, repo)
    }

    suspend fun unstarRepo(owner: String, repo: String): Response<Any> {
        return userService.unstarRepo(owner, repo)
    }

    suspend fun getUserRepoList(page: Int) : List<UserRepo> {
        return userService.getUserRepoList(page = page)
    }

    companion object :
        SingletonHolderDoubleArg<UserRemoteDataSource, LoginService, UserService>(::UserRemoteDataSource)
}

class UserLocalDataSource : ILocalDataSource {

    private val spUtils: SPUtils = SPUtils.getInstance(
        EncryptUtils.encryptMD5ToString("user")
    )

    var userName: String
        get() = spUtils.getString(SP_USER_NAME)
        set(value) = spUtils.put(SP_USER_NAME, value)

    var accessToken: String
        get() = spUtils.getString(SP_ACCESS_TOKEN)
        set(value) = spUtils.put(SP_ACCESS_TOKEN, value)

    companion object {
        val instance = UserLocalDataSource()

        const val SP_ACCESS_TOKEN = "SP_ACCESS_TOKEN"
        const val SP_USER_NAME = "SP_USER_NAME"
    }
}