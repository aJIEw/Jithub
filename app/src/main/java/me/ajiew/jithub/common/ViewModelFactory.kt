package me.ajiew.jithub.common

import androidx.lifecycle.ViewModel
import me.ajiew.core.base.viewmodel.BaseViewModelFactory
import me.ajiew.jithub.data.repository.RepoResolver
import me.ajiew.jithub.ui.explore.ExploreViewModel


class ViewModelFactory : BaseViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        try {
            return when {
                isAssignableFrom(ExploreViewModel::class.java) -> {
                    ExploreViewModel(RepoResolver.exploreRepository) as T
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