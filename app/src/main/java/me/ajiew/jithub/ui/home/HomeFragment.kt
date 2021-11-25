package me.ajiew.jithub.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.hjq.toast.ToastUtils
import me.ajiew.core.base.BaseFragment
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.data.response.FeedsTemplate
import me.ajiew.jithub.databinding.FragmentHomeBinding
import me.ajiew.jithub.util.AppUtil
import java.net.UnknownHostException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Home tab: Recent Activities and Timeline
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.fragment_home
    override val viewModelId: Int = BR.vm
    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.instance
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun initView() {
        super.initView()

        binding.rvTimeline.adapter = viewModel.refreshLoadMoreAdapter
        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.ui.showWebpageUrl.observe(this, { value ->
            if (value != null && value.isNotEmpty()) {
                AppUtil.openCustomTab(requireActivity(), value, useBackIcon = true)
            }
        })
    }

    override fun onSuccess(data: Any, message: String) {
        super.onSuccess(data, message)

        if (data is FeedsTemplate) {

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}