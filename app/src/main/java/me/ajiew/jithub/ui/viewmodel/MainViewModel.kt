package me.ajiew.jithub.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BuildConfig
import me.ajiew.jithub.data.repository.UserRepository

/**
 *
 * @author aJIEw
 * Created on: 2021/11/3 15:22
 */
class MainViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    private var accessToken = ""

    override fun onCreate() {
        super.onCreate()

        accessToken = repository.getAccessToken()
    }

    fun getAccessToken(code: String) {
        uiState.value = UIState.Loading

        viewModelScope.launch {
            val results = repository.requestAuthToken(code)
            uiState.value = when (results) {
                is Results.Success -> {
                    val authToken = results.data

                    accessToken = authToken.access_token
                    repository.saveAccessToken(accessToken)

                    if (BuildConfig.DEBUG) {
                        ToastUtils.show(accessToken)
                    }

                    UIState.Success(results.data, "Get Access Token Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }
}