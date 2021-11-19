package me.ajiew.jithub.widget

import android.content.Context
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.widget.TextView
import com.lxj.xpopup.core.BubbleAttachPopupView
import me.ajiew.core.util.spanny.Spanny
import me.ajiew.jithub.R
import me.ajiew.jithub.data.model.ContributionRecord

/**
 *
 * @author aJIEw
 * Created on: 2021/11/18 16:28
 */
class ContributionAttachBubblePopup(
    context: Context,
    private val contribution: ContributionRecord
) : BubbleAttachPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.view_contribution_popup

    override fun isShowUpToTarget(): Boolean {
        return true
    }

    override fun onCreate() {
        super.onCreate()

        val tvText = findViewById<TextView>(R.id.tv_text)
        val spanny = Spanny()
        if (contribution.number > 0) {
            spanny.append(
                contribution.number.toString() + " "
                        + resources.getQuantityString(R.plurals.contribution, contribution.number),
                StyleSpan(Typeface.BOLD)
            )
        } else {
            spanny.append("No contribution", StyleSpan(Typeface.BOLD))
        }
        spanny.append(" on ${contribution.date}")
        tvText.text = spanny

        tvText.setOnClickListener {
            dismiss()
        }
    }

}