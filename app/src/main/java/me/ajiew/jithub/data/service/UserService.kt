package me.ajiew.jithub.data.service

import me.ajiew.jithub.data.response.AuthToken
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * @author aJIEw
 * Created on: 2021/11/2 17:03
 */
interface UserService {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    suspend fun getAuthToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String
    ): AuthToken
}