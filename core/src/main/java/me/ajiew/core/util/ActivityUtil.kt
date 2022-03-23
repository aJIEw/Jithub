package me.ajiew.core.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 *
 * @author aJIEw
 * Created on: 2021/12/10 15:35
 */
fun setLightStatusAndNavBars(activity: Activity) {
    activity.apply {
        val root = findViewById<View>(android.R.id.content).rootView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.background_light)
            window.statusBarColor = Color.TRANSPARENT
        }
        WindowInsetsControllerCompat(window, root).isAppearanceLightStatusBars = true
        WindowInsetsControllerCompat(window, root).isAppearanceLightNavigationBars = true
    }
}