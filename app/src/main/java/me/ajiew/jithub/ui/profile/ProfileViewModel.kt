package me.ajiew.jithub.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.User

/**
 *
 * @author aJIEw
 * Created on: 2021/11/17 10:08
 */
class ProfileViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    val userInfo = MutableLiveData<User>()

    override fun initFetchData() {
        super.initFetchData()

        fetchUserInfo()
    }

    fun refresh() {
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        uiState.value = UIState.Loading

        viewModelScope.launch {
            val results = repository.requestUserInfo()

            uiState.value = when (results) {
                is Results.Success -> {
                    userInfo.value = results.data!!

                    UIState.Success(results.data, "Load Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }
}