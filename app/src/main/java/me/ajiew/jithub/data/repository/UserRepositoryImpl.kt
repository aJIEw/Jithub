package me.ajiew.jithub.data.repository

import com.blankj.utilcode.util.EncryptUtils
import me.ajiew.core.base.repository.BaseRepositoryBoth
import me.ajiew.core.base.repository.ILocalDataSource
import me.ajiew.core.base.repository.IRemoteDataSource
import me.ajiew.core.data.Results
import me.ajiew.core.util.SPUtils
import me.ajiew.core.util.SingletonHolderDoubleArg
import me.ajiew.core.util.SingletonHolderSingleArg
import me.ajiew.jithub.BuildConfig.GITHUB_CLIENT_ID
import me.ajiew.jithub.BuildConfig.GITHUB_CLIENT_SECRET
import me.ajiew.jithub.data.response.AuthToken
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
        return localDataSource.getAccessToken()
    }

    override fun saveAccessToken(token: String) {
        localDataSource.setAccessToken(token)
    }

    companion object :
        SingletonHolderDoubleArg<UserRepositoryImpl, UserRemoteDataSource, UserLocalDataSource>(::UserRepositoryImpl)
}

class UserRemoteDataSource(private val userService: UserService) : IRemoteDataSource {

    suspend fun getAuthToken(code: String): AuthToken {
        return userService.getAuthToken(
            clientId = GITHUB_CLIENT_ID,
            clientSecret = GITHUB_CLIENT_SECRET,
            code = code
        )
    }

    companion object :
        SingletonHolderSingleArg<UserRemoteDataSource, UserService>(::UserRemoteDataSource)
}

class UserLocalDataSource : ILocalDataSource {

    private val spUtils: SPUtils = SPUtils.getInstance(
        EncryptUtils.encryptMD5ToString("user")
    )

    fun getAccessToken(): String = spUtils.getString(SP_ACCESS_TOKEN)

    fun setAccessToken(token: String) {
        spUtils.put(SP_ACCESS_TOKEN, token)
    }

    companion object {
        val instance = UserLocalDataSource()

        const val SP_ACCESS_TOKEN = "SP_ACCESS_TOKEN"
    }
}