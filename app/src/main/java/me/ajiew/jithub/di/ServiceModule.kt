package me.ajiew.jithub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ajiew.jithub.data.service.LoginService
import me.ajiew.jithub.data.service.NetworkCreator
import me.ajiew.jithub.data.service.TrendingService
import me.ajiew.jithub.data.service.UserService
import javax.inject.Singleton

/**
 *
 * @author aJIEw
 * Created on: 2021/12/6 10:29
 */
@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideTrendingService(): TrendingService {
        return NetworkCreator.createBaseTrending(TrendingService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginService(): LoginService {
        return NetworkCreator.createBaseGithub(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(): UserService {
        return NetworkCreator.createBaseGithubApi(UserService::class.java)
    }
}