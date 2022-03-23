package me.ajiew.jithub.ui.home

import androidx.fragment.app.viewModels
import com.hjq.toast.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.base.BaseFragment
import me.ajiew.core.util.messenger.Messenger
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.MessengerEvent
import me.ajiew.jithub.databinding.FragmentHomeBinding
import me.ajiew.jithub.util.AppUtil
import java.net.UnknownHostException

/**
 * Home tab: Recent Activities and Timeline
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.fragment_home
    override val viewModelId: Int = BR.vm
    override val viewModel: HomeViewModel by viewModels()

    override fun initView() {
        super.initView()

        binding.rvTimeline.adapter = viewModel.refreshLoadMoreAdapter
        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun initBusEventObserver() {
        super.initBusEventObserver()

        Messenger.getDefault().register(this, MessengerEvent.EVENT_TAB_DOUBLE_CLICK_HOME) {
            binding.rvTimeline.scrollToPosition(0)
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showWebpageUrl.observe(this) { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        }
    }

    override fun onFailed(errorData: Any?, message: String) {
        super.onFailed(errorData, message)

        if (errorData is UnknownHostException) {
            ToastUtils.show("Please check your network~")
            viewModel.refreshLoadMoreAdapter.setEmptyView(R.layout.base_empty)
        }
    }

    override fun hideLoading() {
        super.hideLoading()

        binding.refreshLayout.isRefreshing = false
    }
}