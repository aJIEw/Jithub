package me.ajiew.jithub.util

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import me.ajiew.jithub.widget.browser.CustomTabActivityHelper
import me.ajiew.jithub.widget.browser.WebviewFallback

/**
 *
 * @author aJIEw
 * Created on: 2021/11/22 16:33
 */
object AppUtil {
    fun openCustomTab(activity: Activity, url: String, canShare: Boolean = true) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setShareState(if (canShare) CustomTabsIntent.SHARE_STATE_ON else CustomTabsIntent.SHARE_STATE_OFF)
            .build()
        CustomTabActivityHelper.openCustomTab(
            activity, customTabsIntent, Uri.parse(url), WebviewFallback()
        )
    }
}