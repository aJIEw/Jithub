package me.ajiew.core.base.viewmodel

import androidx.annotation.NonNull
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import me.ajiew.core.data.BaseModel
import me.ajiew.core.util.SingleLiveEvent
import java.lang.reflect.InvocationTargetException


/**
 * RecycleView 或者 ListView 的 ItemViewModel，适用于只有一种类型的 ViewModel
 * */
open class ItemViewModel<VM : BaseViewModel<*>>(protected val viewModel: VM) {
}

/**
 * 适用于多种 item 类型的 ViewModel
 * */
open class MultiItemViewModel<VM : BaseViewModel<*>>(@NonNull viewModel: VM) :
    ItemViewModel<VM>(viewModel) {

    var itemType: String = LIST_ITEM_TYPE_CONTENT
}

/**
 * 点击事件监听器，适用于使用 ItemBinding 绑定数据的组件
 */
interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}

const val LIST_ITEM_TYPE_HEADER = "type_header"
const val LIST_ITEM_TYPE_CONTENT = "type_content"
const val LIST_ITEM_TYPE_CONTENT2 = "type_content2"
const val LIST_ITEM_TYPE_CONTENT3 = "type_content3"
const val LIST_ITEM_TYPE_FOOTER = "type_footer"

/**
 * 用于绑定 RecycleView 或者 ListView 的 Header 文字
 */
class ItemHeaderTextViewModel<T : BaseViewModel<*>>(viewModel: T, text: String) :
    MultiItemViewModel<T>(viewModel) {

    val headerText: MutableLiveData<String> = MutableLiveData(text)

    init {
        itemType = LIST_ITEM_TYPE_HEADER
    }
}


/**
 * 用于绑定 RecycleView 或者 ListView 的 Footer 文字
 */
class ItemFooterTextViewModel<T : BaseViewModel<*>>(viewModel: T, text: String) :
    MultiItemViewModel<T>(viewModel) {

    val footerText: MutableLiveData<String> = MutableLiveData(text)

    init {
        itemType = LIST_ITEM_TYPE_FOOTER
    }
}

/**
 * 通过反射的方式将 ItemViewModel 添加进 ObservableList。
 *
 * @param baseViewModel 该 ItemViewModel 的父 ViewModel
 * @param data 数据源，ItemViewModel 中的 model 数据
 * @param args 其它参数
 * */
inline fun <reified VM : ItemViewModel<*>> ObservableList<VM>.addItemViewModels(
    baseViewModel: BaseViewModel<*>,
    data: List<*>,
    vararg args: Any
) {
    if (data.isEmpty()) throw RuntimeException("$data cannot be empty.")

    val viewModelClass = VM::class.java
    try {
        if (args.isEmpty()) {
            val constructor =
                viewModelClass.getConstructor(baseViewModel::class.java, data[0]!!::class.java)

            for (item in data) {
                add(constructor.newInstance(baseViewModel, item))
            }
        } else {
            val argsClasses = mutableListOf<Class<*>>()
            for (arg in args) {
                argsClasses.add(arg::class.java)
            }

            val constructor = viewModelClass.getConstructor(
                baseViewModel::class.java,
                data[0]!!::class.java,
                *(argsClasses.toTypedArray())
            )

            for (item in data) {
                add(constructor.newInstance(baseViewModel, item, args))
            }
        }
    } catch (e: NoSuchMethodException) {
        throw RuntimeException("Cannot create an instance of $viewModelClass", e)
    } catch (e: IllegalAccessException) {
        throw RuntimeException("Cannot create an instance of $viewModelClass", e)
    } catch (e: InstantiationException) {
        throw RuntimeException("Cannot create an instance of $viewModelClass", e)
    } catch (e: InvocationTargetException) {
        throw RuntimeException("Cannot create an instance of $viewModelClass", e)
    }
}
