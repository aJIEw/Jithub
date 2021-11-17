package me.ajiew.jithub.ui.home.timeline

import androidx.lifecycle.MutableLiveData
import com.hjq.toast.ToastUtils
import me.ajiew.core.base.viewmodel.ItemViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
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
    val repo: MutableLiveData<UserRepo> = MutableLiveData()
) : ItemViewModel<HomeViewModel>(vm) {

    val onClickStar = object : OnItemClickListener<Repo> {
        override fun onItemClick(item: Repo) {
            ToastUtils.show("Starred ${item.name}")
        }
    }

    init {
        vm.fetchUserRepoInfo(index, entity.repo.url)
    }
}