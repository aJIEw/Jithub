package me.ajiew.jithub.ui.explore

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.ItemViewModel
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.ExploreRepository
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.BuiltBy
import me.ajiew.jithub.data.response.TrendingRepo
import me.tatarka.bindingcollectionadapter2.ItemBinding
import timber.log.Timber
import javax.inject.Inject


/**
 *
 * @author aJIEw
 * Created on: 2021/10/28 18:19
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: ExploreRepository,
    private val userRepository: UserRepository
) :
    BaseViewModel<ExploreRepository>() {

    val ui = UIChangeObservable()

    var repos = ObservableArrayList<ItemTrendingRepoViewModel>()
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
                        results.data.mapIndexed { index, trendingRepo ->
                            repos.add(
                                ItemTrendingRepoViewModel(
                                    this@ExploreViewModel,
                                    index,
                                    trendingRepo
                                )
                            )
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

    fun hasLoggedIn(): Boolean {
        return UserProfile.accessToken.isNotEmpty()
    }

    fun checkUserStarredRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = userRepository.requestCheckUserStarredRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    repos[index].starred.value = true
                }
            }
        }
    }

    fun requestStarRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = userRepository.requestStarRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    repos[index].starred.value = true
                } else {
                    Timber.e("Something went wrong: ${results.errorBody()}")
                    ToastUtils.show("Something went wrong!")
                }
            }
        }
    }

    fun requestUnstarRepo(index: Int, repo: String) {
        if (!repo.contains("/")) return

        viewModelScope.launch {
            val parts = repo.split("/")

            if (parts.size > 1) {
                val results = userRepository.requestUnstarRepo(parts[0], parts[1])

                if (results.isSuccessful && results.code() == 204) {
                    repos[index].starred.value = false
                } else {
                    Timber.e("Something went wrong: ${results.errorBody()}")
                    ToastUtils.show("Something went wrong!")
                }
            }
        }
    }

    class UIChangeObservable {
        val showLoginPage = SingleLiveEvent<Boolean>()

        val showWebpageUrl = SingleLiveEvent<String>()
    }
}

class ItemTrendingRepoViewModel(
    vm: ExploreViewModel,
    val index: Int,
    val entity: TrendingRepo,
    val starred: SingleLiveEvent<Boolean> = SingleLiveEvent(),
) :
    ItemViewModel<ExploreViewModel>(vm) {

    /**
     * set color after initialization
     * */
    val languageColor = MutableLiveData<String>()

    val contributors = MutableLiveData<List<BuiltBy>>()
    val contributorsBinding = ItemBinding.of<BuiltBy>(BR.item, R.layout.item_trending_built_by)

    private val repo = entity.author + "/" + entity.name

    init {
        languageColor.value = entity.languageColor ?: "#000000"
        contributors.value = entity.builtBy.take(5)

        viewModel.checkUserStarredRepo(index, repo)
    }

    fun onClickItem(view: View) {
        viewModel.ui.showWebpageUrl.value = entity.url
    }

    fun onClickStar(view: View) {
        if (viewModel.hasLoggedIn()) {
            if (starred.value == true) {
                viewModel.requestUnstarRepo(index, repo)
            } else {
                viewModel.requestStarRepo(index, repo)
            }
        } else {
            viewModel.ui.showLoginPage.value = true
        }
    }

}