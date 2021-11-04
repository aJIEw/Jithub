package me.ajiew.jithub.data.repository

import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.data.Results
import me.ajiew.jithub.data.response.AuthToken

/**
 *
 * @author aJIEw
 * Created on: 2021/11/2 17:03
 */
interface UserRepository : IRepository {

    suspend fun requestAuthToken(code: String): Results<AuthToken>

    fun getAccessToken(): String

    fun saveAccessToken(token: String)
}