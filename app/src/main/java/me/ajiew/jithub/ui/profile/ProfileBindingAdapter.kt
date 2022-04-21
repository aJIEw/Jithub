package me.ajiew.jithub.ui.profile

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ConvertUtils
import me.ajiew.core.util.spanny.Spanny
import me.ajiew.jithub.R

/**
 *
 * @author aJIEw
 * Created on: 2021/11/18 18:13
 */
@BindingAdapter("contributionTotal")
fun setTotalContributionNumber(textView: TextView, totalContribution: Int) {
    val spanny = Spanny()
    spanny.append(
        totalContribution.toString(),
        StyleSpan(Typeface.BOLD),
        RelativeSizeSpan(1.2f)
    ).append(
        " " + textView.resources.getQuantityString(R.plurals.contribution, totalContribution),
        StyleSpan(Typeface.BOLD),
    ).append(" in the last ").append("90", StyleSpan(Typeface.BOLD)).append(" days")

    textView.text = spanny
}

@BindingAdapter(value = ["contribution", "maxDailyContribution", "minDailyContribution"], requireAll = true)
fun setContributionBlockBgColor(textView: TextView, contribution: Int, maxDailyContribution: Int, minDailyContribution: Int) {
    if (maxDailyContribution <= 0 || minDailyContribution <= 0) {
        return
    }

    val step = (maxDailyContribution - minDailyContribution) / 3
    val minLevel = minDailyContribution + step
    val midLevel = minDailyContribution + step * 2
    val maxLevel = minDailyContribution + step * 3

    val drawable = GradientDrawable()
    drawable.cornerRadius = ConvertUtils.dp2px(3f).toFloat()
    drawable.setColor(when {
        contribution >= maxLevel -> textView.resources.getColor(R.color.color_contribution_very_good)
        contribution >= midLevel -> textView.resources.getColor(R.color.color_contribution_good)
        contribution >= minLevel -> textView.resources.getColor(R.color.color_contribution_well)
        contribution > 0 -> textView.resources.getColor(R.color.color_contribution)
        contribution == 0 -> textView.resources.getColor(R.color.color_contribution_zero)
        else -> textView.resources.getColor(R.color.white)
    })
    textView.background = drawable
}