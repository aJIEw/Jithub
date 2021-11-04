package me.ajiew.jithub.ui.explore

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.ItemViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.repository.ExploreRepository
import me.ajiew.jithub.data.response.BuiltBy
import me.ajiew.jithub.data.response.TrendingRepo
import me.tatarka.bindingcollectionadapter2.ItemBinding


/**
 *
 * @author aJIEw
 * Created on: 2021/10/28 18:19
 */
class ExploreViewModel(private val repository: ExploreRepository) :
    BaseViewModel<ExploreRepository>() {

    var repos = MutableLiveData<List<ItemTrendingRepoViewModel>>(emptyList())
    var reposBinding = ItemBinding.of<ItemTrendingRepoViewModel>(BR.vm, R.layout.item_trending_repo)

    override fun initFetchData() {
        fetchData()
    }

    fun fetchData() {
        uiState.value = UIState.Loading

        viewModelScope.launch {
            val results = repository.requestTrendingRepos("", "")
            uiState.value = when (results) {
                is Results.Success -> {
                    if (results.data.isNotEmpty()) {
                        repos.value = results.data.mapIndexed { index, trendingRepo ->
                            ItemTrendingRepoViewModel(this@ExploreViewModel, trendingRepo, index)
                        }
                        UIState.Success(results.data, "Load Success")
                    } else {
                        UIState.Error(message = "No data")
                    }
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }
}

class ItemTrendingRepoViewModel(
    viewModel: ExploreViewModel,
    val entity: TrendingRepo,
    val index: Int
) :
    ItemViewModel<ExploreViewModel>(viewModel) {

    /**
     * set color after initialization
     * */
    val languageColor = MutableLiveData<String>()

    val contributors = MutableLiveData<List<BuiltBy>>()
    val contributorsBinding = ItemBinding.of<BuiltBy>(BR.item, R.layout.item_trending_built_by)

    init {
        languageColor.value = entity.languageColor ?: "#000000"
        contributors.value = entity.builtBy.take(5)
    }

    fun onClickItem(view: View) {
        ToastUtils.show("Clicked ${entity.name} at ${index + 1}")
    }

    fun onClickStar(view: View) {
        ToastUtils.show("Starred ${entity.name}")
    }

}