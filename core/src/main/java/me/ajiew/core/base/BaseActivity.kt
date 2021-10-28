package me.ajiew.core.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.BaseViewModel.Companion.BUNDLE
import me.ajiew.core.base.viewmodel.BaseViewModel.Companion.CANONICAL_NAME
import me.ajiew.core.base.viewmodel.BaseViewModel.Companion.CLASS
import me.ajiew.core.util.messenger.Messenger

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>> :
    AppCompatActivity(), IBaseView<VM> {

    protected lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activity 初始化参数
        initParam()

        // 初始化 DataBinding 和页面事件回调监听
        initViewDataBinding()
        registerUIChangeLiveDataCallBack()

        // 页面事件监听的方法，一般用于注册 ViewModel 传到 View 层的事件
        initViewObservable()

        // 注册点击事件监听器
        onViewClick()

        // 页面数据初始化方法
        initData()

        // 视图初始化方法
        initView()
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
        /*if (viewModel == null) {
            val modelClass: Class<*>
            val type = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<*>
            } else {
                // 如果没有指定泛型参数，则默认使用 BaseViewModel
                BaseViewModel::class.java
            }
            @Suppress("UNCHECKED_CAST")
            viewModel = createViewModel(modelClass as Class<ViewModel>) as VM
        }*/

        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }

    fun refreshLayout() {
        binding.setVariable(viewModelId, viewModel)
    }

    protected fun registerUIChangeLiveDataCallBack() {
        viewModel.ui.startActivityEvent.observe(this) { params ->
            val clz = params[CLASS] as Class<*>
            val bundle = params[BUNDLE] as Bundle?
            startActivity(clz, bundle)
        }

        viewModel.ui.startContainerActivityEvent.observe(this) { params ->
            val canonicalName = params[CANONICAL_NAME] as String?
            val bundle = params[BUNDLE] as Bundle?
            startContainerActivity(canonicalName, bundle)
        }

        viewModel.ui.finishEvent.observe(this) { finish() }

        viewModel.ui.onBackPressedEvent.observe(this) { onBackPressed() }
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的 Activity 类
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名: Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    @JvmOverloads
    fun startContainerActivity(canonicalName: String?, bundle: Bundle? = null) {
        if (canonicalName == null) {
            throw ClassNotFoundException("Class name must not be null!")
        }

        val intent = Intent(this, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName)
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.unbind()

        Messenger.getDefault().unregister(viewModel)
    }

    /**
     * 创建 ViewModel
     */
    protected fun <T : ViewModel> createViewModel(cls: Class<T>): T {
        return ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(cls)
    }
}