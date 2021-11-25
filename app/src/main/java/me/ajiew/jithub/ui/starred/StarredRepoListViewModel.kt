package me.ajiew.jithub.ui.starred

import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.jithub.base.ListRefreshLoadMoreAdapter
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.UserRepo
import me.ajiew.jithub.ui.repo.RepoListViewModel

/**
 *
 * @author aJIEw
 * Created on: 2021/11/24 16:32
 */
class StarredRepoListViewModel(private val userRepository: UserRepository) :
    RepoListViewModel(userRepository) {

    override fun initAdapter(): ListRefreshLoadMoreAdapter<UserRepo, *> =
        StarredRepoListAdapter(object :
            OnItemClickListener<UserRepo> {
            override fun onItemClick(item: UserRepo) {
                ui.showWebpageUrl.value = item.html_url
            }
        })

    override suspend fun requestListData(page: Int, perPage: Int): Results<List<UserRepo>> =
        userRepository.requestUserStarredRepoList(page)
}