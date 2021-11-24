package me.ajiew.jithub.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.Constants.BASE_GITHUB_URL
import me.ajiew.jithub.data.model.GithubEvent
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.service.UserService
import me.ajiew.jithub.ui.home.timeline.ItemTimelineViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding
import timber.log.Timber

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 15:51
 */
class HomeViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    val ui = UIChangeObservable()

    var timelineFeeds = MutableLiveData<List<EventTimeline>>(emptyList())
    var timelineBinding = ItemBinding.of<EventTimeline>(BR.vm, R.layout.item_feeds_timeline)
    var timelineAdapter = FeedsTimelineAdapter(object : OnItemClickListener<EventTimeline> {
        override fun onItemClick(item: EventTimeline) {
            Timber.d("Clicked ${item.repo.name}")
            ui.showWebpageUrl.value = BASE_GITHUB_URL + item.repo.name
        }
    }).apply {
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
                        val addedNum = timelineAdapter.data.size
                        timelineAdapter.addData(data.mapIndexed { index, event ->
                            ItemTimelineViewModel(this@HomeViewModel, addedNum + index, event)
                        })
                    }

                    if (data.size < UserService.RESULTS_PER_PAGE) {
                        timelineAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        timelineAdapter.loadMoreModule.loadMoreComplete()
                    }

                    timelinePage++

                    UIState.Success(data, "Load Success")
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

    fun checkUserStarredRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = repository.requestCheckUserStarredRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    timelineAdapter.getItem(index).starred.value = true
                    timelineAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    fun requestStarRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = repository.requestStarRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    timelineAdapter.getItem(index).starred.value = true
                    timelineAdapter.notifyItemChanged(index)
                } else {
                    Timber.e("Something went wrong: ${results.errorBody()}")
                    ToastUtils.show("Something went wrong!")
                }
            }
        }
    }

    fun requestUnstarRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = repository.requestUnstarRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    timelineAdapter.getItem(index).starred.value = false
                    timelineAdapter.notifyItemChanged(index)
                } else {
                    Timber.e("Something went wrong: ${results.errorBody()}")
                    ToastUtils.show("Something went wrong!")
                }
            }
        }
    }

    class UIChangeObservable {
        val showWebpageUrl = SingleLiveEvent<String>()
    }

    companion object {
        val TIMELINE_EVENTS: List<String> =
            listOf(
                GithubEvent.WatchEvent.type,
                GithubEvent.ForkEvent.type,
                GithubEvent.ReleaseEvent.type,
                GithubEvent.CreateEvent.type,
                GithubEvent.PublicEvent.type
            )
    }
}