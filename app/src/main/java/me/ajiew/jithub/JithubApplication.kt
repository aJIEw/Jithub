package me.ajiew.jithub

import android.app.Application
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import me.ajiew.core.base.BaseApplication

/**
 *
 * @author aJIEw
 * Created on: 2021/10/28 14:15
 */
@HiltAndroidApp
class JithubApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
    }
}