package me.ajiew.jithub.ui.repo

import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.UserRepo
import me.ajiew.jithub.data.service.UserService

/**
 *
 * @author aJIEw
 * Created on: 2021/11/23 12:14
 */
class RepoListViewModel(private val userRepository: UserRepository) :
    BaseViewModel<UserRepository>() {

    val ui = UIChangeObservable()

    val repoListAdapter = RepoListAdapter(object : OnItemClickListener<UserRepo> {
        override fun onItemClick(item: UserRepo) {
            if (item.visibility == "public") {
                ui.showWebpageUrl.value = item.html_url
            } else {
                ToastUtils.show("Sorry, can't open private repositories right now.")
            }
        }
    }).apply {
        loadMoreModule.setOnLoadMoreListener {
            loadMore()
        }
    }

    private var page = 1
    private var isRefreshing = false

    override fun initFetchData() {
        super.initFetchData()

        refresh()
    }

    fun refresh() {
        isRefreshing = true
        page = 1

        uiState.value = UIState.Loading

        fetchRepoList()
    }

    fun loadMore() {
        fetchRepoList()
    }

    private fun fetchRepoList() {
        viewModelScope.launch {
            val results = userRepository.requestUserRepoList(page)

            uiState.value = when (results) {
                is Results.Success -> {
                    val data = results.data.toList()
                    if (isRefreshing) {
                        isRefreshing = false

                        repoListAdapter.setList(data)
                        repoListAdapter.loadMoreModule.isEnableLoadMore = true
                    } else {
                        repoListAdapter.addData(data)
                    }

                    if (data.size < UserService.RESULTS_PER_PAGE) {
                        repoListAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        repoListAdapter.loadMoreModule.loadMoreComplete()
                    }

                    page++

                    UIState.Success(data, "Load Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }

    class UIChangeObservable {
        val showWebpageUrl = SingleLiveEvent<String>()
    }
}