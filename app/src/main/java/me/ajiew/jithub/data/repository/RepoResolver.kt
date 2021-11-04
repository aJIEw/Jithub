package me.ajiew.jithub.data.repository

import me.ajiew.jithub.data.service.ServiceResolver

/**
 *
 * @author aJIEw
 * Created on: 2021/5/28 17:03
 */
object RepoResolver {

    val exploreRepository by lazy {
        ExploreRepositoryImpl.getInstance(
            ExploreRemoteDataSource.getInstance(ServiceResolver.trendingService),
        )
    }

    val userRepository by lazy {
        UserRepositoryImpl.getInstance(
            UserRemoteDataSource.getInstance(ServiceResolver.userService),
            UserLocalDataSource.instance
        )
    }

}