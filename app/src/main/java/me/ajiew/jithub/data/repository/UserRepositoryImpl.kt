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
import me.ajiew.jithub.data.response.AuthToken
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.FeedsTemplate
import me.ajiew.jithub.data.service.LoginService
import me.ajiew.jithub.data.service.UserService
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

    override suspend fun requestUserTimeline(name: String): Results<List<EventTimeline>> {
        return try {
            val result = remoteDataSource.getUserTimeline(name)
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

    suspend fun getUserFeeds(): FeedsTemplate {
        return userService.getUserFeeds()
    }

    suspend fun getUserTimeline(userName: String): List<EventTimeline> {
        return userService.getUserTimeline(userName)
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