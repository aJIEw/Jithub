package me.ajiew.jithub.ui.home

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import me.ajiew.core.util.spanny.Spanny
import me.ajiew.jithub.R
import me.ajiew.jithub.data.response.EventTimeline
import me.ajiew.jithub.databinding.ItemFeedsTimelineBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/11 14:38
 */
class FeedsTimelineAdapter :
    BaseQuickAdapter<EventTimeline, BaseDataBindingHolder<ItemFeedsTimelineBinding>>(R.layout.item_feeds_timeline),
    LoadMoreModule {

    override fun convert(
        holder: BaseDataBindingHolder<ItemFeedsTimelineBinding>,
        item: EventTimeline
    ) {
        holder.dataBinding?.apply {
            vm = item
            executePendingBindings()
        }

        val event = Spanny(item.actor.login, StyleSpan(Typeface.BOLD)).append(" ")
        setEventMessage(event, item)
        holder.setText(R.id.tv_event, event)
    }

    private fun setEventMessage(spanny: Spanny, event: EventTimeline) {

        var repoName = event.repo.name
        when (event.type) {
            "WatchEvent" -> {
                spanny.append("starred ")
                    .append(repoName, StyleSpan(Typeface.BOLD))
            }
            "ForkEvent" -> {
                repoName = event.payload.forkee?.full_name ?: ""
                spanny.append("forked ")
                    .append(repoName, StyleSpan(Typeface.BOLD))
            }
            "ReleaseEvent" -> {
                spanny.append("released ")
                if (event.payload.action == "published" && event.payload.release != null) {
                    spanny.append(" ")
                        .append(event.payload.release.tag_name, StyleSpan(Typeface.BOLD))
                        .append(" of ")
                        .append(repoName, StyleSpan(Typeface.BOLD))
                }
            }
            "CreateEvent" -> {
                if (event.payload.ref_type == "repository") {
                    spanny.append("created a repository ")
                        .append(repoName, StyleSpan(Typeface.BOLD))
                }
            }
            "PublicEvent" -> {
                spanny.append("made ")
                    .append(repoName, StyleSpan(Typeface.BOLD))
                    .append(" public")
            }
            else -> {
                spanny.append("?")
            }
        }
    }

}