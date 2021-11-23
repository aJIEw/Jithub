package me.ajiew.jithub.ui.explore

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.github.nukc.stateview.StateView
import com.hjq.toast.ToastUtils
import me.ajiew.core.base.BaseFragment
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.data.response.TrendingRepo
import me.ajiew.jithub.databinding.FragmentExploreBinding
import me.ajiew.jithub.util.AppUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Explore tab
 */
class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreViewModel>() {

    override val layoutId: Int = R.layout.fragment_explore

    override val viewModelId: Int = BR.vm

    override val viewModel: ExploreViewModel by viewModels {
        ViewModelFactory.instance
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showLoginPage.observe(this, { value ->
            if (value == true) {
                val url: String = Constants.GITHUB_OAUTH_AUTHORIZE_URL
                AppUtil.openCustomTab(requireActivity(), url, false)
            }
        })

        viewModel.ui.showWebpageUrl.observe(this, { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        })
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}