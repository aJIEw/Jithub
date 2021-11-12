package me.ajiew.jithub.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.service.UserService
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

    override fun initFetchData() {
        super.initFetchData()

        fetchHomeData()
    }

    fun refresh() {
        fetchHomeData()
    }

    fun loadMore() {
        viewModelScope.launch {
            fetchUserFeeds()
        }
    }

    private fun fetchHomeData() {
        fetchUserFeeds()
    }

    private fun fetchUserFeeds() {
        uiState.value = UIState.Loading

        viewModelScope.launch {
            val results = repository.requestUserFeeds()
            uiState.value = when (results) {
                is Results.Success -> {
                    val feedsTemplate = results.data
                    val userUrl = feedsTemplate.current_user_public_url
                    if (userUrl != null) {
                        val name = userUrl.substring(userUrl.lastIndexOf("/") + 1)
                        userName = name
                        repository.saveUserName(name)
                    }

                    fetchUserTimeline()

                    UIState.Success(results.data, "Load Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }

    private suspend fun fetchUserTimeline() {
        val results = repository.requestUserTimeline(userName, timelinePage)
        if (results is Results.Success && results.data.isNotEmpty()) {

            val data = filterRepos(results.data.toList())
            if (timelineFeeds.value!!.isEmpty()) {
                timelineAdapter.setList(data)
                timelineAdapter.loadMoreModule.isEnableLoadMore = true
                timelineFeeds.value = data
            } else {
                timelineAdapter.addData(data)
            }

            if (data.size < UserService.RESULTS_PER_PAGE) {
                timelineAdapter.loadMoreModule.loadMoreEnd()
            } else {
                timelineAdapter.loadMoreModule.loadMoreComplete()
            }

            timelinePage++
        }
    }

    private fun filterRepos(list: List<EventTimeline>): List<EventTimeline> {
        return list.filter { TIMELINE_EVENTS.contains(it.type) }
    }

    companion object {
        /**
         * GitHub event types:
         * https://docs.github.com/en/developers/webhooks-and-events/events/github-event-types
         * */
        val TIMELINE_EVENTS: List<String> =
            listOf("WatchEvent", "ForkEvent", "ReleaseEvent", "CreateEvent", "PublicEvent")
    }
}