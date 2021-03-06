package me.ajiew.jithub.common

import androidx.lifecycle.ViewModel
import me.ajiew.core.base.viewmodel.BaseViewModelFactory
import me.ajiew.jithub.data.repository.RepoResolver
import me.ajiew.jithub.ui.explore.ExploreViewModel
import me.ajiew.jithub.ui.home.HomeViewModel
import me.ajiew.jithub.ui.profile.ProfileViewModel
import me.ajiew.jithub.ui.repo.RepoListViewModel
import me.ajiew.jithub.ui.starred.StarredRepoListViewModel
import me.ajiew.jithub.ui.viewmodel.MainViewModel

/**
 * Use ViewModelFactory to init ViewModel like this:
 * ```kotlin
 * val viewModel: MyViewModel by viewModels() {
 *     ViewModelFactory.instance
 * }
 * ```
 * */
class ViewModelFactory : BaseViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        try {
            return when {
                isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(RepoResolver.userRepository) as T
                }
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(RepoResolver.userRepository) as T
                }
                isAssignableFrom(ExploreViewModel::class.java) -> {
                    ExploreViewModel(
                        RepoResolver.exploreRepository,
                        RepoResolver.userRepository
                    ) as T
                }
                isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(RepoResolver.userRepository) as T
                }
                isAssignableFrom(RepoListViewModel::class.java) -> {
                    RepoListViewModel(RepoResolver.userRepository) as T
                }
                isAssignableFrom(StarredRepoListViewModel::class.java) -> {
                    StarredRepoListViewModel(RepoResolver.userRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: $name")
            }
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }

    companion object {
        val instance = ViewModelFactory()
    }
}