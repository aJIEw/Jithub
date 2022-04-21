package me.ajiew.jithub.ui.profile

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
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
import me.ajiew.jithub.ui.repo.ARG_TITLE
import me.ajiew.jithub.ui.repo.RepoListFragment
import me.ajiew.jithub.ui.starred.StarredRepoListFragment
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.floor

/**
 *
 * @author aJIEw
 * Created on: 2021/11/17 10:08
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<UserRepository>() {

    val ui = UIChangeObservable()

    val userInfo = SingleLiveEvent<User>()

    val contributionLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val contributionLabelsBinding =
        ItemBinding.of<String>(BR.item, R.layout.item_contribution_label)

    val contributionList: ObservableList<ItemContributionBrick> = ObservableArrayList()
    val _contributionList = mutableListOf<ContributionRecord>()
    val contributionBinding =
        ItemBinding.of<ItemContributionBrick>(BR.item, R.layout.item_contribution_brick)

    val optionsList: ObservableList<ProfileOptionItem> = ObservableArrayList()
    val optionsBinding = ItemBinding.of<ProfileOptionItem>(BR.item, R.layout.item_profile_option)

    var totalContributions = SingleLiveEvent<Int>()
    var contributionPlaceholderDays = 0
    private var contributionEventPage = 1

    init {
        initOptions()
    }

    override fun initFetchData() {
        super.initFetchData()

        refresh()
    }

    private fun initContributionData() {
        totalContributions.value = 0
        contributionPlaceholderDays = 0
        contributionEventPage = 1

        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        _contributionList.clear()
        contributionList.clear()

        contributionPlaceholderDays = when (today.dayOfWeek) {
            DayOfWeek.SUNDAY -> 6
            DayOfWeek.MONDAY -> 5
            DayOfWeek.TUESDAY -> 4
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 2
            DayOfWeek.FRIDAY -> 1
            DayOfWeek.SATURDAY -> 0
            else -> 0
        }

        var startIndex = 7
        val dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        // For the latest week, if shorter than 7 day, add placeholder record (number = -1)
        if (contributionPlaceholderDays > 0) {
            val totalOffset = 6 - contributionPlaceholderDays
            for (i in 0..totalOffset) {
                val offset = totalOffset - i
                val date = today.minusDays(offset.toLong()).format(dateFormat)
                _contributionList.add(ContributionRecord(i, date, 0))
            }

            for (i in (7 - contributionPlaceholderDays) until 7) {
                _contributionList.add(ContributionRecord(i, "", -1))
            }
        } else {
            startIndex = 0
        }

        // 15 weeks at most
        for (i in startIndex until 105 step 7) {
            val weekEndIndex = i + 6
            var weekStartIndex = i
            for (offset in weekEndIndex downTo i) {
                val date =
                    today.minusDays((offset - contributionPlaceholderDays).toLong())
                        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                _contributionList.add(ContributionRecord(weekStartIndex, date, 0))
                weekStartIndex++
            }
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

        addOption(
            R.drawable.shape_option_repo,
            StringUtils.getString(R.string.page_title_repo),
            onClick = {
                val bundle = Bundle()
                bundle.putString(ARG_TITLE, StringUtils.getString(R.string.page_title_repo))
                startContainerActivity(RepoListFragment::class.qualifiedName, bundle)
            })
        addOption(
            R.drawable.shape_option_starred,
            StringUtils.getString(R.string.page_title_starred),
            onClick = {
                val bundle = Bundle()
                bundle.putString(ARG_TITLE, StringUtils.getString(R.string.page_title_starred))
                startContainerActivity(StarredRepoListFragment::class.qualifiedName, bundle)
            })
        /*addOption(R.drawable.shape_option_settings, "Settings", onClick = {
            ToastUtils.show("show settings")
        })*/
    }

    fun refresh() {
        contributionEventPage = 1

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
            getUserContributionRequest()
        }
    }

    private suspend fun getUserContributionRequest() {
        val results = repository.requestUserEvent(contributionEventPage)
        if (results is Results.Success && results.data.isNotEmpty()) {
            val data = results.data

            filterPushEvent(data)

            if (data.size > 99) {
                contributionEventPage++
                getUserContributionRequest()
            } else {
                var total = 0
                var max = 0
                var min = 0
                _contributionList.forEach {
                    total += if (it.number > -1) it.number else 0

                    if (it.number > max) {
                        max = it.number
                    }

                    min = if (min == 0 && it.number > 0) it.number else min
                    if (it.number in 1 until min) {
                        min = it.number
                    }
                }

                _contributionList.forEach {
                    contributionList.add(ItemContributionBrick(this, it, max, min))
                }

                totalContributions.value = total
                ui.contributionDataFetched.value = true
            }
        }
        ui.contributionFetching.value = false
    }

    private fun filterPushEvent(data: List<EventTimeline>) {
        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

        val firstWeekDays = 7 - contributionPlaceholderDays
        fun updateContributionNumber(updateIndex: Int, commits: List<Commit>?) {
            val contribution = _contributionList[updateIndex]
            contribution.number += filterCurrentUserCommits(commits)
            _contributionList[updateIndex] = contribution
        }
        data.map { item ->
            if (item.type == GithubEvent.PushEvent.type) {
                val date =
                    Instant.parse(item.created_at).atZone(ZoneId.systemDefault()).toLocalDate()
                // days in between is the index of this date in the contribution list
                val daysInBetween = Duration.between(date.atStartOfDay(), today.atStartOfDay())
                    .toDays().toInt()
                if (daysInBetween in 0 until firstWeekDays) {
                    val updateIndex = firstWeekDays - 1 - daysInBetween
                    updateContributionNumber(updateIndex, item.payload.commits)
                } else if (daysInBetween >= firstWeekDays) {
                    // reverse column index
                    val total = daysInBetween + contributionPlaceholderDays
                    val mid = floor(total / 7.0) * 7 + 3;
                    var updateIndex = mid;
                    if (total > mid) {
                        updateIndex = mid - (total - mid);
                    } else if (total < mid) {
                        updateIndex = mid + (mid - total);
                    }
                    updateContributionNumber(updateIndex.toInt(), item.payload.commits)
                }
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

    fun onClickTotalContribution(view: View) {
        ui.showContributionExplanation.value = true
    }

    class UIChangeObservable {
        val contributionDataFetched = SingleLiveEvent<Boolean>()

        val contributionFetching = SingleLiveEvent<Boolean>()

        val showContributionPopup = SingleLiveEvent<ContributionRecord>()

        val showContributionExplanation = SingleLiveEvent<Boolean>()
    }
}