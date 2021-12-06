package me.ajiew.jithub.ui.profile

import android.os.Bundle
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
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.databinding.FragmentProfileBinding
import me.ajiew.jithub.widget.ContributionAttachBubblePopup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Profile tab: User info
 */
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val layoutId: Int = R.layout.fragment_profile
    override val viewModelId: Int = BR.vm
    override val viewModel: ProfileViewModel by viewModels()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var basePopupView: BasePopupView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        viewModel.ui.contributionDataFetched.observe(this, { value ->
            // show today's contribution popup once data is available
            if (value == true && isVisible) {
                val firstWeekDays = 7 - viewModel.contributionPlaceholderDays
                viewModel.ui.showContributionPopup.value =
                    viewModel.contributionList.subList(0, firstWeekDays).maxByOrNull { it.index }
            }
        })

        viewModel.ui.showContributionPopup.observe(this, { value ->
            if (value != null) {
                XPopup.Builder(requireActivity())
                    .atView(binding.rvContribution.findViewHolderForLayoutPosition(value.index)?.itemView)
                    .hasShadowBg(false)
                    .isViewMode(true)
                    .isDestroyOnDismiss(true)
                    .asCustom(ContributionAttachBubblePopup(requireActivity(), value))
                    .show()
            }
        })

        viewModel.ui.showContributionExplanation.observe(this) { value ->
            if (value != null) {
                basePopupView = XPopup.Builder(requireActivity())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}