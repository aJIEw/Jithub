package me.ajiew.jithub.ui.profile

import me.ajiew.core.base.viewmodel.ItemViewModel
import me.ajiew.core.base.viewmodel.OnItemClickListener
import me.ajiew.jithub.data.model.ContributionRecord

/**
 *
 * @author aJIEw
 * Created on: 2022/4/21 10:24
 */
class ItemContributionBrick(
    vm: ProfileViewModel,
    var entity: ContributionRecord,
    var maxDailyContribution: Int,
    var minDailyContribution: Int
) : ItemViewModel<ProfileViewModel>(vm) {

    var onClickItem = object : OnItemClickListener<ContributionRecord> {
        override fun onItemClick(item: ContributionRecord) {
            if (item.date.isNotEmpty() && item.number != -1) {
                vm.ui.showContributionPopup.value = item
            }
        }
    }
}