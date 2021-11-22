package me.ajiew.jithub.ui.profile

import androidx.annotation.DrawableRes
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch
import me.ajiew.core.base.UIState
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.core.data.Results
import me.ajiew.core.util.SingleLiveEvent
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.data.model.ContributionRecord
import me.ajiew.jithub.data.model.GithubEvent
import me.ajiew.jithub.data.model.ProfileOptionItem
import me.ajiew.jithub.data.model.UserProfile
import me.ajiew.jithub.data.repository.UserRepository
import me.ajiew.jithub.data.response.Commit
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.data.response.User
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 *
 * @author aJIEw
 * Created on: 2021/11/17 10:08
 */
class ProfileViewModel(private val repository: UserRepository) : BaseViewModel<UserRepository>() {

    val ui = UIChangeObservable()

    val userInfo = SingleLiveEvent<User>()

    val contributionLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val contributionLabelsBinding =
        ItemBinding.of<String>(BR.item, R.layout.item_contribution_label)

    val contributionList: ObservableList<ContributionRecord> = ObservableArrayList()
    val contributionBinding =
        ItemBinding.of<ContributionRecord>(BR.item, R.layout.item_contribution_brick)
            .bindExtra(BR.onClickItem, object : OnItemClickListener<ContributionRecord> {
                override fun onItemClick(item: ContributionRecord) {
                    if (item.date.isNotEmpty() && item.number != -1) {
                        ui.showContributionPopup.value = item
                    }
                }
            })

    val optionsList: ObservableList<ProfileOptionItem> = ObservableArrayList()
    val optionsBinding = ItemBinding.of<ProfileOptionItem>(BR.item, R.layout.item_profile_option)

    var totalContributions = SingleLiveEvent<Int>()
    private var contributionPlaceholderDays = 0
    private var contributionEventPage = 1

    init {
        initContributionData()

        initOptions()
    }

    override fun initFetchData() {
        super.initFetchData()

        refresh()
    }

    private fun initContributionData() {
        totalContributions.value = 0

        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        contributionList.clear()

        contributionPlaceholderDays = when (today.dayOfWeek) {
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 5
            DayOfWeek.SUNDAY -> 6
            else -> 0
        }

        if (contributionPlaceholderDays > 0) {
            for (i in 0 until contributionPlaceholderDays) {
                contributionList.add(ContributionRecord(i, "", -1))
            }
        }
        for (i in 0..89) {
            val date =
                today.minusDays(i.toLong()).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            contributionList.add(ContributionRecord(i + contributionPlaceholderDays, date, 0))
        }
    }

    private fun initOptions() {
        fun addOption(
            @DrawableRes icon: Int,
            title: String,
            endText: String = "",
            onClick: () -> Unit
        ) {
            optionsList.add(
                ProfileOptionItem(
                    icon,
                    title,
                    endText,
                    object : OnItemClickListener<ProfileOptionItem> {
                        override fun onItemClick(item: ProfileOptionItem) {
                            onClick()
                        }
                    })
            )
        }

        addOption(R.drawable.shape_option_repo, "Repositories", onClick = {
            ToastUtils.show("show repositories")
        })
        addOption(R.drawable.shape_option_starred, "Starred", onClick = {
            ToastUtils.show("show starred repos")
        })
        addOption(R.drawable.shape_option_settings, "Settings", onClick = {
            ToastUtils.show("show settings")
        })
    }

    fun refresh() {
        fetchUserInfo()

        initContributionData()

        fetchUserContributionData()
    }

    private fun fetchUserInfo() {
        uiState.value = UIState.Loading

        viewModelScope.launch {
            val results = repository.requestUserInfo()

            uiState.value = when (results) {
                is Results.Success -> {
                    userInfo.value = results.data!!

                    initRepoNumber(results.data)

                    UIState.Success(results.data, "Load Success")
                }
                is Results.Error -> UIState.Error(null, results.message)
            }
        }
    }

    private fun initRepoNumber(userInfo: User) {
        val total = (userInfo.total_private_repos ?: 0) + (userInfo.public_repos ?: 0)
        if (total > 0) {
            val repo = optionsList[0]
            repo.endText = total.toString()
            optionsList[0] = repo
        }
    }

    private fun fetchUserContributionData() {
        ui.contributionFetching.value = true

        viewModelScope.launch {
            val results = repository.requestUserEvent(contributionEventPage)
            if (results is Results.Success && results.data.isNotEmpty()) {
                val data = results.data

                filterPushEvent(data)

                ui.contributionFetching.value = false

                if (data.size > 99) {
                    contributionEventPage++
                    fetchUserContributionData()
                }
            }
        }
    }

    private fun filterPushEvent(data: List<EventTimeline>) {
        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

        data.mapIndexed { index, item ->
            if (item.type == GithubEvent.PushEvent.type) {
                val date =
                    Instant.parse(item.created_at).atZone(ZoneId.systemDefault()).toLocalDate()
                val contributionIndex = Duration.between(date.atStartOfDay(), today.atStartOfDay())
                    .toDays().toInt() + contributionPlaceholderDays
                val contribution = contributionList[contributionIndex]

                contribution.number = filterCurrentUserCommits(item.payload.commits)
                // update total contribution number
                totalContributions.value = totalContributions.value!! + contribution.number
                contributionList[contributionIndex] = contribution
            }
        }
    }

    private fun filterCurrentUserCommits(commits: List<Commit>?): Int {
        if (commits == null) return 0

        return commits.count {
            // TODO: 2021/11/18 filter by user specified email address,  so we can include
            //  the commits made by the same author with different email account
            it.author.name == UserProfile.userName
        }
    }

    class UIChangeObservable {
        val contributionFetching = SingleLiveEvent<Boolean>()

        val showContributionPopup = SingleLiveEvent<ContributionRecord>()
    }
}