package me.ajiew.jithub.ui.explore

import androidx.fragment.app.viewModels
import com.github.nukc.stateview.StateView
import com.hjq.toast.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.base.BaseFragment
import me.ajiew.core.util.messenger.Messenger
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.data.MessengerEvent
import me.ajiew.jithub.data.response.TrendingRepo
import me.ajiew.jithub.databinding.FragmentExploreBinding
import me.ajiew.jithub.util.AppUtil

/**
 * Explore tab
 */
@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreViewModel>() {

    override val layoutId: Int = R.layout.fragment_explore

    override val viewModelId: Int = BR.vm

    override val viewModel: ExploreViewModel by viewModels()

    override fun initView() {
        super.initView()

        binding.pullLayout.setActionListener {
            binding.pullLayout.apply {
                postDelayed({
                    finishActionRun(it)
                }, 800)
            }
        }
    }

    override fun initBusEventObserver() {
        super.initBusEventObserver()

        Messenger.getDefault().register(this, MessengerEvent.EVENT_TAB_DOUBLE_CLICK_EXPLORE) {
            binding.rvExplore.scrollToPosition(0)
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showLoginPage.observe(this) { value ->
            if (value == true) {
                val url: String = Constants.GITHUB_OAUTH_AUTHORIZE_URL
                AppUtil.openCustomTab(requireActivity(), url, false)
            }
        }

        viewModel.ui.showWebpageUrl.observe(this) { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(data: Any, message: String) {
        super.onSuccess(data, message)

        val repos = data as List<TrendingRepo>
    }

    override fun onFailed(errorData: Any?, message: String) {
        super.onFailed(errorData, message)

        ToastUtils.show(message)
        binding.stateView.showRetry()
        binding.stateView.onRetryClickListener = object : StateView.OnRetryClickListener {
            override fun onRetryClick() {
                viewModel.fetchData()
            }
        }
    }

    override fun showLoading() {
        super.showLoading()
        binding.stateView.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
        binding.stateView.showContent()
    }
}