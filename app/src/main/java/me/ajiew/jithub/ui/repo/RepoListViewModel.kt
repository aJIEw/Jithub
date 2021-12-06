package me.ajiew.jithub.ui.repo

import com.hjq.toast.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.base.ListRefreshLoadMoreAdapter
import me.ajiew.jithub.base.ListRefreshLoadMoreViewModel
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.UserRepo
import javax.inject.Inject

/**
 *
 * @author aJIEw
 * Created on: 2021/11/23 12:14
 */
@HiltViewModel
open class RepoListViewModel @Inject constructor(private val userRepository: UserRepository) :
    ListRefreshLoadMoreViewModel<UserRepository, UserRepo, UserRepo>() {

    val ui = UIChangeObservable()

    override fun initAdapter(): ListRefreshLoadMoreAdapter<UserRepo, *> =
        RepoListAdapter(object : OnItemClickListener<UserRepo> {
            override fun onItemClick(item: UserRepo) {
                if (item.visibility == "public") {
                    ui.showWebpageUrl.value = item.html_url
                } else {
                    ToastUtils.show("Sorry, can't open private repositories right now.")
                }
            }
        })

    override fun initFetchData() {
        super.initFetchData()

        refresh()
    }

    override suspend fun requestListData(page: Int, perPage: Int): Results<Collection<UserRepo>> =
        userRepository.requestUserRepoList(page)

    override fun convertToTargetList(
        isRefreshing: Boolean,
        list: List<UserRepo>
    ): Collection<UserRepo> = list

    class UIChangeObservable {
        val showWebpageUrl = SingleLiveEvent<String>()
    }
}