package me.ajiew.jithub.data.repository

import me.ajiew.core.base.repository.BaseRepositoryRemote
import me.ajiew.core.base.repository.IRemoteDataSource
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingletonHolderSingleArg
import me.ajiew.jithub.data.response.BuiltBy
import me.ajiew.jithub.data.response.TrendingRepo
import me.ajiew.jithub.data.service.TrendingService
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @author aJIEw
 * Created on: 2021/6/11 11:17
 */
class ExploreRepositoryImpl @Inject constructor(remoteDataSource: ExploreRemoteDataSource) :
    ExploreRepository,
    BaseRepositoryRemote<ExploreRemoteDataSource>(remoteDataSource) {

    override suspend fun requestTrendingRepos(
        language: String,
        spokenLanguageCode: String
    ): Results<List<TrendingRepo>> {
        return try {
            val result = remoteDataSource.fetchRepos(language, spokenLanguageCode)
            Results.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            Results.Error(e)
        }
    }

    companion object :
        SingletonHolderSingleArg<ExploreRepositoryImpl, ExploreRemoteDataSource>(::ExploreRepositoryImpl)
}

class ExploreRemoteDataSource @Inject constructor(private val trendingService: TrendingService) :
    IRemoteDataSource {

    suspend fun fetchRepos(language: String, spokenLanguageCode: String): List<TrendingRepo> {
        /*if (BuildConfig.DEBUG) {
            delay(500)
            return getDummySuccessData()
        }*/

        return trendingService.fetchRepos(
            language = language,
            spokenLanguageCode = spokenLanguageCode
        )
    }

    fun getDummySuccessData(): List<TrendingRepo> = MutableList(5) {
        TrendingRepo(
            author = "aJIEw",
            avatar = "https://avatars1.githubusercontent.com/u/13328707?s=200&v=4",
            builtBy = listOf(
                BuiltBy("https://avatars.githubusercontent.com/u/31198074", "", "czj2369"),
                BuiltBy("https://avatars.githubusercontent.com/u/5053714", "", "nuta"),
                BuiltBy("https://avatars.githubusercontent.com/u/27057", "", "temoto"),
                BuiltBy("https://avatars.githubusercontent.com/u/2322099", "", "momo0853"),
            ),
            currentPeriodStars = 1,
            description = "Github Android Client App.",
            forks = 1,
            language = if (it != 3) "Kotlin" else null,
            languageColor = if (it % 2 == 0) "#f1e05a" else null,
            name = "Jithub $it",
            stars = 101,
            url = "https://github.com/aJIEw"
        )
    }

    companion object :
        SingletonHolderSingleArg<ExploreRemoteDataSource, TrendingService>(::ExploreRemoteDataSource)
}