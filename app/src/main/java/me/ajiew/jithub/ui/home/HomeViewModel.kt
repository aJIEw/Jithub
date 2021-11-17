package me.ajiew.jithub.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.service.UserService
import me.ajiew.jithub.ui.home.timeline.ItemTimelineViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 15:51
 */
class HomeViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    var timelineFeeds = MutableLiveData<List<EventTimeline>>(emptyList())
    var timelineBinding = ItemBinding.of<EventTimeline>(BR.vm, R.layout.item_feeds_timeline)
    var timelineAdapter = FeedsTimelineAdapter().apply {
        loadMoreModule.setOnLoadMoreListener {
            loadMore()
        }
    }

    private var userName: String = ""
    private var timelinePage = 1
    private var isRefreshing = false

    init {
        userName = UserProfile.userName
    }

    override fun initFetchData() {
        super.initFetchData()

        fetchHomeData()
    }

    fun refresh() {
        isRefreshing = true
        timelinePage = 1

        fetchHomeData()
    }

    fun loadMore() {
        fetchUserTimeline()
    }

    private fun fetchHomeData() {
        uiState.value = UIState.Loading

        fetchUserTimeline()
    }

    private fun fetchUserTimeline() {
        viewModelScope.launch {
            val results = repository.requestUserTimeline(userName, timelinePage)
            uiState.value = when (results) {
                is Results.Success -> {
                    val data = filterRepos(results.data.toList())
                    if (timelineFeeds.value!!.isEmpty() || isRefreshing) {
                        timelineAdapter.setList(data.mapIndexed { index, event ->
                            ItemTimelineViewModel(this@HomeViewModel, index, event)
                        })
                        timelineAdapter.loadMoreModule.isEnableLoadMore = true
                        timelineFeeds.value = data
                        isRefreshing = false
                    } else {
                        timelineAdapter.addData(data.mapIndexed { index, event ->
                            ItemTimelineViewModel(this@HomeViewModel, index, event)
                        })
                    }

                    if (data.size < UserService.RESULTS_PER_PAGE) {
                        timelineAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        timelineAdapter.loadMoreModule.loadMoreComplete()
                    }

                    timelinePage++

                    UIState.Success(results.data, "Load Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }

    private fun filterRepos(list: List<EventTimeline>): List<EventTimeline> {
        return list.filter { TIMELINE_EVENTS.contains(it.type) }
    }

    fun fetchUserRepoInfo(index: Int, url: String) {
        viewModelScope.launch {
            val results = repository.requestUserRepo(url)
            if (results is Results.Success) {
                val data = results.data
                timelineAdapter.getItem(index).repo.value = data
                timelineAdapter.notifyItemChanged(index)
            }
        }
    }

    companion object {
        /**
         * GitHub event types:
         * @link https://docs.github.com/en/developers/webhooks-and-events/events/github-event-types
         * */
        val TIMELINE_EVENTS: List<String> =
            listOf("WatchEvent", "ForkEvent", "ReleaseEvent", "CreateEvent", "PublicEvent")
    }
}