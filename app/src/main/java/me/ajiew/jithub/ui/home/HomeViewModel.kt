package me.ajiew.jithub.ui.home

import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.base.ListRefreshLoadMoreAdapter
import me.ajiew.jithub.base.ListRefreshLoadMoreViewModel
import me.ajiew.jithub.common.Constants.BASE_GITHUB_URL
import me.ajiew.jithub.data.model.GithubEvent
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.ui.home.timeline.ItemTimelineViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 15:51
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: UserRepository) :
    ListRefreshLoadMoreViewModel<UserRepository, ItemTimelineViewModel, EventTimeline>() {

    val ui = UIChangeObservable()

    private var userName: String = UserProfile.userName

    override fun initFetchData() {
        super.initFetchData()

        refresh()
    }

    override fun initAdapter(): ListRefreshLoadMoreAdapter<ItemTimelineViewModel, *> =
        FeedsTimelineAdapter(object : OnItemClickListener<EventTimeline> {
            override fun onItemClick(item: EventTimeline) {
                Timber.d("Clicked ${item.repo.name}")
                ui.showWebpageUrl.value = BASE_GITHUB_URL + item.repo.name
            }
        })

    override suspend fun requestListData(
        page: Int,
        perPage: Int
    ): Results<Collection<EventTimeline>> = repository.requestUserTimeline(userName, page)

    override fun processRawData(data: List<EventTimeline>): List<EventTimeline> {
        return data.filter { TIMELINE_EVENTS.contains(it.type) }
    }

    override fun convertToTargetList(
        isRefreshing: Boolean,
        list: List<EventTimeline>
    ): Collection<ItemTimelineViewModel> =
        list.mapIndexed { index, event ->
            ItemTimelineViewModel(
                this@HomeViewModel,
                (if (isRefreshing) 0 else refreshLoadMoreAdapter.data.size) + index,
                event
            )
        }

    fun fetchUserRepoInfo(index: Int, url: String) {
        viewModelScope.launch {
            val results = repository.requestUserRepo(url)
            if (results is Results.Success) {
                val data = results.data
                refreshLoadMoreAdapter.getItem(index).repo.value = data
                refreshLoadMoreAdapter.notifyItemChanged(index)
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
                    refreshLoadMoreAdapter.getItem(index).starred.value = true
                    refreshLoadMoreAdapter.notifyItemChanged(index)
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
                    refreshLoadMoreAdapter.getItem(index).starred.value = true
                    refreshLoadMoreAdapter.notifyItemChanged(index)
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
                    refreshLoadMoreAdapter.getItem(index).starred.value = false
                    refreshLoadMoreAdapter.notifyItemChanged(index)
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