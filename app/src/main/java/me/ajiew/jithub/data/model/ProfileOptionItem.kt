package me.ajiew.jithub.data.model

import androidx.annotation.DrawableRes
import me.ajiew.core.base.viewmodel.OnItemClickListener

/**
 *
 * @author aJIEw
 * Created on: 2021/11/19 12:00
 */
data class ProfileOptionItem(
    @DrawableRes var icon: Int,
    var title: String,
    var endText: String,
    var onClickItem: OnItemClickListener<ProfileOptionItem>?
)