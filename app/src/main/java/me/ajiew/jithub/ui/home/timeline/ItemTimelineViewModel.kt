package me.ajiew.jithub.ui.home.timeline

import me.ajiew.core.base.viewmodel.ItemViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.Repo
import me.ajiew.jithub.data.response.UserRepo
import me.ajiew.jithub.ui.home.HomeViewModel

/**
 *
 * @author aJIEw
 * Created on: 2021/11/15 18:19
 */
class ItemTimelineViewModel(
    vm: HomeViewModel,
    val index: Int = -1,
    val entity: EventTimeline,
    val repo: SingleLiveEvent<UserRepo> = SingleLiveEvent(),
    val starred: SingleLiveEvent<Boolean> = SingleLiveEvent(),
) : ItemViewModel<HomeViewModel>(vm) {

    val onClickStar = object : OnItemClickListener<Repo> {
        override fun onItemClick(item: Repo) {
            if (starred.value == true) {
                viewModel.requestUnstarRepo(index, entity.repo.name)
            } else {
                viewModel.requestStarRepo(index, entity.repo.name)
            }
        }
    }

    init {
        viewModel.checkUserStarredRepo(index, entity.repo.name)

        viewModel.fetchUserRepoInfo(index, entity.repo.url)
    }
}