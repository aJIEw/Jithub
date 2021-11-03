package me.ajiew.core.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.multidex.MultiDex
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import com.hjq.toast.ToastUtils
import me.ajiew.core.BuildConfig
import me.ajiew.core.util.AppManager
import okhttp3.OkHttpClient
import timber.log.Timber

/**
 *
 * @author aJIEw
 * Created on: 2021/6/7 18:01
 */
open class BaseApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)

        setApplication(this)

        ToastUtils.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }

    companion object {
        private var sInstance: Application? = null

        @SuppressLint("StaticFieldLeak")
        private var sContext: Context? = null

        /**
         * 当主工程没有继承 BaseApplication 时，可以使用 setApplication() 方法初始化 BaseApplication
         *
         * @param application
         */
        @Synchronized
        fun setApplication(@NonNull application: Application) {
            sInstance = application
            sContext = application.applicationContext

            // 注册监听每个 activity 的生命周期
            application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    AppManager.instance.addActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {
                    AppManager.instance.removeActivity(activity)
                }
            })
        }

        /**
         * 获得当前 app 运行的 Application
         */
        val instance: Application
            get() {
                if (sInstance == null) {
                    throw NullPointerException("please inherit BaseApplication or call setApplication.")
                }
                return sInstance!!
            }

        /**
         * 获得全局 Context
         */
        val context: Context
            get() {
                if (sContext == null) {
                    throw NullPointerException("please inherit BaseApplication or call setApplication.")
                }
                return sContext!!
            }
    }
}