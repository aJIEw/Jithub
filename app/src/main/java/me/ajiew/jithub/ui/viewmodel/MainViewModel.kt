package me.ajiew.jithub.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.data.Results
import me.ajiew.jithub.BuildConfig
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @author aJIEw
 * Created on: 2021/11/3 15:22
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<UserRepository>() {

    private var accessToken = ""

    override fun onCreate() {
        super.onCreate()

        accessToken = repository.getAccessToken()

        // Initiate username if has logged in before
        if (UserProfile.userName.isEmpty()) {
            repository.getUserName()
        }
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
                        Timber.d(accessToken)
                    }

                    getUserName()

                    UIState.Success(results.data, "Get Access Token Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }

    private suspend fun getUserName() {
        val userName = repository.getUserName()
        if (userName.isEmpty()) {
            val results = repository.requestUserFeeds()
            if (results is Results.Success) {
                val feedsTemplate = results.data
                val userUrl = feedsTemplate.current_user_public_url
                if (userUrl != null) {
                    val name = userUrl.substring(userUrl.lastIndexOf("/") + 1)
                    repository.saveUserName(name)
                }
            }
        }
    }

}