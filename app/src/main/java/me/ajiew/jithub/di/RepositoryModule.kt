package me.ajiew.jithub.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.ajiew.jithub.data.repository.ExploreRepository
import me.ajiew.jithub.data.repository.ExploreRepositoryImpl
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.repository.UserRepositoryImpl
import me.ajiew.jithub.data.response.UserRepo

/**
 *
 * @author aJIEw
 * Created on: 2021/12/6 10:29
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindExploreRepository(
        exploreRepositoryImpl: ExploreRepositoryImpl
    ): ExploreRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}