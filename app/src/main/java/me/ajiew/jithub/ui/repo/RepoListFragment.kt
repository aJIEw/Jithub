package me.ajiew.jithub.ui.repo

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import me.ajiew.core.base.BaseFragment
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.databinding.FragmentRepoListBinding
import me.ajiew.jithub.util.AppUtil

/**
 *
 * @author aJIEw
 * Created on: 2021/11/23 12:14
 */
class RepoListFragment : BaseFragment<FragmentRepoListBinding, RepoListViewModel>() {

    override val layoutId: Int = R.layout.fragment_repo_list
    override val viewModelId: Int = BR.vm
    override val viewModel: RepoListViewModel by viewModels {
        ViewModelFactory.instance
    }

    override fun initView() {
        super.initView()

        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.rvRepoList.adapter = viewModel.repoListAdapter
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showWebpageUrl.observe(this, { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        })
    }

    override fun hideLoading() {
        super.hideLoading()

        binding.refreshLayout.isRefreshing = false
    }
}