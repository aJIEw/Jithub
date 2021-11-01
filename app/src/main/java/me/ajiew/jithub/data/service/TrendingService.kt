package me.ajiew.jithub.data.service

import me.ajiew.jithub.data.response.TrendingRepo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * @author aJIEw
 * Created on: 2021/5/28 15:07
 */
interface TrendingService {
    @GET("repositories")
    suspend fun fetchRepos(
        @Query("since") since: String = "daily",
        @Query("language") language: String,
        @Query("spoken_language_code") spokenLanguageCode: String
    ): List<TrendingRepo>
}