package me.ajiew.core.base

import androidx.lifecycle.LifecycleOwner
import com.hjq.toast.ToastUtils
import me.ajiew.core.base.viewmodel.BaseViewModel

interface IBaseView<VM : BaseViewModel<*>> : LifecycleOwner {

    /**
     * 初始化根布局 id
     *
     * @return 布局 layout 的 id
     */
    val layoutId: Int

    /**
     * 初始化 ViewModel 的 id
     *
     * @return BR 的 id
     */
    val viewModelId: Int

    /**
     * 初始化 ViewModel，推荐使用懒加载的方式初始化
     *
     * @return 继承自 BaseViewModel 的 ViewModel
     */
    val viewModel: VM

    /**
     * 初始化界面传递参数
     */
    fun initParam() {}

    /**
     * 初始化数据
     */
    fun initData() {}

    /**
     * 初始化视图
     */
    fun initView() {}

    /**
     * 初始化界面 UI 状态的监听
     */
    fun initViewObservable() {
        viewModel.uiState().observe(this) { uiState ->
            @Suppress("UNCHECKED_CAST")
            when (uiState) {
                is UIState.Loading -> showLoading()
                is UIState.Success -> onSuccess(uiState.data, uiState.message)
                is UIState.Error -> onFailed(uiState.errorData, uiState.message)
                is UIState.Message -> showMessage(uiState.message)
            }
        }
    }

    /**
     * 展示消息提醒，默认当 message 不为空的时候显示一个 Toast 消息
     * */
    fun showMessage(message: String) {
        hideLoading()

        if (message.isNotBlank()) {
            ToastUtils.show(message)
        }
    }

    fun onSuccess(data: Any, message: String) {
        hideLoading()
    }

    fun onFailed(errorData: Any?, message: String) {
        hideLoading()
    }

    fun showLoading() {}

    fun hideLoading() {}

    /**
     * View 点击事件的监听
     * */
    fun onViewClick() {}
}