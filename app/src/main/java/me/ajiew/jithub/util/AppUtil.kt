package me.ajiew.jithub.util

import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import me.ajiew.jithub.R
import me.ajiew.jithub.widget.browser.CustomTabActivityHelper
import me.ajiew.jithub.widget.browser.WebviewFallback

/**
 *
 * @author aJIEw
 * Created on: 2021/11/22 16:33
 */
object AppUtil {
    fun openCustomTab(
        activity: Activity,
        url: String,
        canShare: Boolean = true,
        useBackIcon: Boolean = false
    ) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .apply {
                if (useBackIcon) setCloseButtonIcon(
                    BitmapFactory.decodeResource(
                        activity.resources,
                        R.drawable.ic_arrow_back
                    )
                )
            }
            .setShareState(if (canShare) CustomTabsIntent.SHARE_STATE_ON else CustomTabsIntent.SHARE_STATE_OFF)
            .build()
        CustomTabActivityHelper.openCustomTab(
            activity, customTabsIntent, Uri.parse(url), WebviewFallback()
        )
    }
}