package me.ajiew.jithub.ui.profile

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
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