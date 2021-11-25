package me.ajiew.jithub.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results

/**
 * ViewModel's default implementation for refresh and load more.
 *
 * @param T the repository
 * @param IT list item's type
 * @param M model data's type from the request result
 */
abstract class ListRefreshLoadMoreViewModel<T : IRepository, IT, M> :
    BaseViewModel<T>() {

    val refreshLoadMoreAdapter = initAdapter().apply {
        loadMoreModule.setOnLoadMoreListener {
            loadMore()
        }
    }

    open val perPage = 30

    private var page = 1
    private var isRefreshing = false

    abstract fun initAdapter(): ListRefreshLoadMoreAdapter<IT, *>

    abstract suspend fun requestListData(page: Int, perPage: Int): Results<Collection<M>>

    /**
     * Override this if you need to process data before using it, for example, filtering data.
     * */
    open fun processRawData(data: List<M>): List<M> = data

    abstract fun convertToTargetList(isRefreshing: Boolean, list: List<M>): Collection<IT>

    open fun loadMore() {
        fetchListData()
    }

    open fun refresh() {
        isRefreshing = true
        page = 1

        uiState.value = UIState.Loading

        fetchListData()
    }

    open fun fetchListData() {
        viewModelScope.launch {
            val results = requestListData(page, perPage)

            uiState.value = when (results) {
                is Results.Success -> {
                    val data = processRawData(results.data.toList())
                    if (isRefreshing) {
                        isRefreshing = false

                        refreshLoadMoreAdapter.setList(convertToTargetList(true, data))
                        refreshLoadMoreAdapter.loadMoreModule.isEnableLoadMore = true
                    } else {
                        refreshLoadMoreAdapter.addData(convertToTargetList(false, data))
                    }

                    if (data.size < perPage) {
                        refreshLoadMoreAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        refreshLoadMoreAdapter.loadMoreModule.loadMoreComplete()
                    }

                    page++

                    UIState.Success(data, "Load Success")
                }
                is Results.Error -> UIState.Error(results.error, results.message)
            }
        }
    }
}