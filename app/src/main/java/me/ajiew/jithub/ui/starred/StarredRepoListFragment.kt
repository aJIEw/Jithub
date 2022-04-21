package me.ajiew.jithub.ui.starred

import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ConvertUtils
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.base.BaseFragment
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.databinding.FragmentRepoListBinding
import me.ajiew.jithub.ui.repo.ARG_TITLE
import me.ajiew.jithub.util.AppUtil

/**
 *
 * @author aJIEw
 * Created on: 2021/11/24 17:14
 */
@AndroidEntryPoint
class StarredRepoListFragment : BaseFragment<FragmentRepoListBinding, StarredRepoListViewModel>() {

    override val layoutId: Int = R.layout.fragment_repo_list
    override val viewModelId: Int = BR.vm
    override val viewModel: StarredRepoListViewModel by viewModels()

    private var title: String? = null

    override fun initParam() {
        super.initParam()

        arguments?.let {
            title = it.getString(ARG_TITLE)
        }
    }

    override fun initView() {
        super.initView()

        title?.let {
            viewModel.title.value = it
        }
        binding.refreshLayout.setProgressViewOffset(false,
                -ConvertUtils.dp2px(5f), ConvertUtils.dp2px(20f))
        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.rvRepoList.adapter = viewModel.refreshLoadMoreAdapter
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showWebpageUrl.observe(this) { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()

        binding.refreshLayout.isRefreshing = false
    }
}