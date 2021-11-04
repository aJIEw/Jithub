package me.ajiew.jithub.data.repository

import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.data.Results
import me.ajiew.jithub.data.response.TrendingRepo


interface ExploreRepository : IRepository {

    suspend fun requestTrendingRepos(
        language: String,
        spokenLanguageCode: String
    ): Results<List<TrendingRepo>>
}