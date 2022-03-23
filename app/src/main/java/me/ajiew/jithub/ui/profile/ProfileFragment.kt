package me.ajiew.jithub.ui.profile

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.base.BaseFragment
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.databinding.FragmentProfileBinding
import me.ajiew.jithub.widget.ContributionAttachBubblePopup

/**
 * Profile tab: User info
 */
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val layoutId: Int = R.layout.fragment_profile
    override val viewModelId: Int = BR.vm
    override val viewModel: ProfileViewModel by viewModels()

    private var explainPopup: BasePopupView? = null
    private var contributionPopup: BasePopupView? = null

    override fun initView() {
        super.initView()

        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.rvContribution.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.HORIZONTAL, true)
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.contributionDataFetched.observe(this) { value ->
            // show today's contribution popup once data is available
            if (value == true && isVisible) {
                val firstWeekDays = 7 - viewModel.contributionPlaceholderDays
                viewModel.ui.showContributionPopup.value =
                    viewModel.contributionList.subList(0, firstWeekDays).maxByOrNull { it.index }
            }
        }

        viewModel.ui.showContributionPopup.observe(this) { value ->
            if (value != null && (contributionPopup == null || !contributionPopup!!.isShow)) {
                contributionPopup = XPopup.Builder(requireActivity())
                    .atView(binding.rvContribution.findViewHolderForLayoutPosition(value.index)?.itemView)
                    .hasShadowBg(false)
                    .isViewMode(true)
                    .isDestroyOnDismiss(true)
                    .asCustom(ContributionAttachBubblePopup(requireActivity(), value))
                    .show()
            }
        }

        viewModel.ui.showContributionExplanation.observe(this) { value ->
            if (value != null) {
                explainPopup = XPopup.Builder(requireActivity())
                    .isDestroyOnDismiss(true)
                    .asConfirm(
                        getString(R.string.dialog_contribution_explanation),
                        null,
                        null,
                        null,
                        null,
                        null,
                        true,
                        R.layout.dialog_confirm
                    )
                    .show()
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()

        binding.refreshLayout.isRefreshing = false
    }

    override fun onFailed(errorData: Any?, message: String) {
        super.onFailed(errorData, message)

        binding.loadingViewContribution.visibility = View.GONE
    }
}