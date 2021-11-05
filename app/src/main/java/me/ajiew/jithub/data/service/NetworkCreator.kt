package me.ajiew.jithub.data.service

import me.ajiew.jithub.BuildConfig
import me.ajiew.jithub.data.repository.RepoResolver
import me.ajiew.jithub.data.service.interceptor.AuthHeaderInterceptor
import me.ajiew.jithub.data.service.interceptor.GithubExpiredInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


object NetworkCreator {

    private const val BASE_GITHUB_URL = "https://github.com/"

    private const val BASE_API_URL = "https://api.github.com/"

    private const val BASE_TRENDING_URL = "https://gtrend.yapie.me/"


    private const val DEFAULT_TIMEOUT: Long = 10

    private const val KEEP_ALIVE_DURATION: Long = 60

    private const val MAX_IDLE_CONNECTION = 8

    private const val HTTP_CACHE_SIZE: Long = (24 * 1024 * 1024).toLong()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor { Timber.tag("OkHttp").v(it) }
                .setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                ))
        .addInterceptor(GithubExpiredInterceptor())
//        .addInterceptor(AuthHeaderInterceptor(RepoResolver.userRepository))
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(MAX_IDLE_CONNECTION, KEEP_ALIVE_DURATION, TimeUnit.SECONDS))
        .build()

    private val baseGithubRetrofit = Retrofit.Builder()
        .baseUrl(BASE_GITHUB_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val baseGithubApiRetrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val baseTrendingRetrofit = Retrofit.Builder()
        .baseUrl(BASE_TRENDING_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <T> createBaseGithub(serviceClass: Class<T>): T = baseGithubRetrofit.create(serviceClass)

    fun <T> createBaseGithubApi(serviceClass: Class<T>): T = baseGithubApiRetrofit.create(serviceClass)

    fun <T> createBaseTrending(serviceClass: Class<T>): T = baseTrendingRetrofit.create(serviceClass)
}