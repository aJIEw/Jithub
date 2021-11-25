package me.ajiew.jithub.ui.starred

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.jithub.R
import me.ajiew.jithub.base.ListRefreshLoadMoreAdapter
import me.ajiew.jithub.data.response.UserRepo
import me.ajiew.jithub.databinding.ItemStarredRepoBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/24 16:46
 */
class StarredRepoListAdapter(private val onClickItem: OnItemClickListener<UserRepo>) :
    ListRefreshLoadMoreAdapter<UserRepo, ItemStarredRepoBinding>(R.layout.item_starred_repo) {

    override fun convert(holder: BaseDataBindingHolder<ItemStarredRepoBinding>, item: UserRepo) {
        holder.dataBinding?.apply {
            this.item = item
            this.onClickItem = this@StarredRepoListAdapter.onClickItem
            executePendingBindings()
        }
    }
}