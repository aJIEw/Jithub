package me.ajiew.core.base.viewmodel

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.hjq.toast.ToastUtils
import me.ajiew.core.base.UIState
import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.core.util.messenger.BindingAction
import me.ajiew.core.util.messenger.Messenger
import java.util.*

/**
 * T 表示当前 ViewModel 依赖的主 Repository，只是作为标记。见 [IRepository][IRepository]
 * */
open class BaseViewModel<in T : IRepository> : ViewModel(), IBaseViewModel {

    var onBackPressed = BindingAction { onBackPressed() }

    /**
     * 页面标题，可在视图初始化时进行设置，比如从其它页面跳转传递过来的数据。
     * 可在 xml 中直接绑定到组件上。
     * */
    val title = SingleLiveEvent<String>()

    internal val ui: UIChangeObservable = UIChangeObservable()

    protected val uiState = SingleLiveEvent<UIState>()
    internal fun uiState() = uiState

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}

    override fun onCreate() {
        initFetchData()
    }

    override fun onStart() {}

    /**
     * 初始化时的加载数据
     *
     * 调用发生于 onCreate() 之后，确保在页面事件监听器初始化之后才被调用
     * */
    protected open fun initFetchData() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {}

    open fun showToast(message: String) {
        ToastUtils.show(message)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的 Activity 类
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val params: MutableMap<String, Any> = HashMap()
        params[CLASS] = clz
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        ui.startActivityEvent.postValue(params)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     * @param lightStatusAndNavBars 状态栏和导航栏浅色主题，5.0 以上系统状态栏背景白色、图标黑色，白色导航栏，默认为 true
     */
    @JvmOverloads
    fun startContainerActivity(
        canonicalName: String?,
        bundle: Bundle? = null,
        lightStatusAndNavBars: Boolean = true
    ) {
        if (canonicalName == null) {
            throw ClassNotFoundException("Class name must not be null!")
        }

        val params: MutableMap<String, Any> = HashMap()
        params[CANONICAL_NAME] = canonicalName
        if (bundle != null) {
            params[BUNDLE] = bundle
            params[LIGHT_BARS] = lightStatusAndNavBars
        }
        ui.startContainerActivityEvent.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        ui.finishEvent.call()
    }

    /**
     * 返回上一层
     */
    private fun onBackPressed() {
        ui.onBackPressedEvent.call()
    }

    override fun onCleared() {
        super.onCleared()

        Messenger.getDefault().unregister(this)
    }

    companion object {
        var CLASS = "CLASS"
        var CANONICAL_NAME = "CANONICAL_NAME"
        var BUNDLE = "BUNDLE"
        var LIGHT_BARS = "LIGHT_STATUS_BAR"
    }

    class UIChangeObservable {

        val startActivityEvent: SingleLiveEvent<Map<String, Any>> = SingleLiveEvent()

        val startContainerActivityEvent: SingleLiveEvent<Map<String, Any>> = SingleLiveEvent()

        val finishEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }
}