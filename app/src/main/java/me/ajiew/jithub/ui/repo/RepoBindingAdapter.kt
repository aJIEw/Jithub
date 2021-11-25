package me.ajiew.jithub.ui.repo

import android.widget.TextView
import androidx.databinding.BindingAdapter
import me.ajiew.jithub.util.getFriendlyTime


@BindingAdapter("repoUpdatedAt")
fun setRepoUpdateTime(textView: TextView, updatedAt: String) {
    var updateTime = "Updated "
    val time = getFriendlyTime(updatedAt)
    if (!time.endsWith("day") && !time.endsWith("ago")) {
        updateTime += "on "
    }
    textView.text = updateTime + time
}