package me.ajiew.jithub.common

import androidx.lifecycle.ViewModel
import me.ajiew.core.base.viewmodel.BaseViewModelFactory


class ViewModelFactory : BaseViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        try {
            return when {
                /*isAssignableFrom(PagingViewModel::class.java) -> {
                    PagingViewModel(RepoResolver.githubRepository) as T
                }*/
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