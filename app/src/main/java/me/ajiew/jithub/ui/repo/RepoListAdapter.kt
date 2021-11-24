package me.ajiew.jithub.ui.repo

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.jithub.R
import me.ajiew.jithub.data.response.UserRepo
import me.ajiew.jithub.databinding.ItemRepoListBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/23 14:21
 */
class RepoListAdapter(private val onClickItem: OnItemClickListener<UserRepo>) :
    BaseQuickAdapter<UserRepo, BaseDataBindingHolder<ItemRepoListBinding>>(R.layout.item_repo_list),
    LoadMoreModule {

    override fun convert(holder: BaseDataBindingHolder<ItemRepoListBinding>, item: UserRepo) {
        holder.dataBinding?.apply {
            this.item = item
            this.onClickItem = this@RepoListAdapter.onClickItem
            executePendingBindings()
        }
    }
}