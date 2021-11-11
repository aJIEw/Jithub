package me.ajiew.jithub.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.EventTimeline
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/4 15:51
 */
class HomeViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    var timelineEvents = MutableLiveData<List<EventTimeline>>(emptyList())
    var timelineBinding = ItemBinding.of<EventTimeline>(BR.vm, R.layout.item_event_timeline)

    private var userName: String = ""

    override fun initFetchData() {
        super.initFetchData()

        fetchHomeData()
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
        val results = repository.requestUserTimeline(userName)
        if (results is Results.Success && results.data.isNotEmpty()) {
            timelineEvents.value = results.data.toList()

            ToastUtils.show("fetched ${results.data.size} items")
        }
    }
}