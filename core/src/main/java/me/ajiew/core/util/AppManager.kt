package me.ajiew.core.util

import android.app.Activity
import androidx.fragment.app.Fragment
import java.util.*


class AppManager private constructor() {

    /**
     * 添加 Activity 到堆栈
     */
    fun addActivity(activity: Activity?) {
        activityStack.add(activity)
    }

    /**
     * 移除指定的 Activity
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
        }
    }

    /**
     * 是否有 Activity
     */
    fun hasActivity(): Boolean {
        return !activityStack.isEmpty()
    }

    /**
     * 获取当前 Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前 Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的 Activity
     */
    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有 Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                finishActivity(activityStack[i])
            }
            i++
        }
        activityStack.clear()
    }

    /**
     * 获取指定的 Activity
     */
    fun getActivity(cls: Class<*>): Activity? {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }

    /**
     * 添加 Fragment 到堆栈
     */
    fun addFragment(fragment: Fragment) {
        fragmentStack.add(fragment)
    }

    /**
     * 移除指定的 Fragment
     */
    fun removeFragment(fragment: Fragment) {
        fragmentStack.remove(fragment)
    }

    /**
     * 是否有 Fragment
     */
    fun hasFragment(): Boolean {
        return !fragmentStack.isEmpty()
    }

    /**
     * 获取当前 Fragment（堆栈中最后一个压入的）
     */
    fun currentFragment(): Fragment {
        return fragmentStack.lastElement()
    }

    /**
     * 获取指定的 Fragment
     */
    fun getFragment(cls: Class<*>): Fragment? {
        for (fragment in fragmentStack) {
            if (fragment.javaClass == cls) {
                return fragment
            }
        }
        return null
    }

    /**
     * 退出应用程序
     */
    fun appExit() {
        try {
            finishAllActivity()
            // 杀死该应用进程
            //android.os.Process.killProcess(android.os.Process.myPid());

            //System.exit(0);
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n);

//            finish() 是 Activity 的类方法，仅针对 Activity
//            当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；
//            当调用 System.exit(0) 时，会退出当前 Activity 并释放资源（内存），但是该方法不可以结束整个 App。
//            如有多个 Activity 或者有其他组件 service 等时不会结束。
//            其实 android 的机制决定了用户无法完全退出应用，当你的 application 长时间没有被用过的时候，
//            android 自身会决定何时将 application 退出释放资源。
        } catch (e: Exception) {
            activityStack.clear()
            e.printStackTrace()
        }
    }

    companion object {
        var activityStack: Stack<Activity> = Stack()
        var fragmentStack: Stack<Fragment> = Stack()
        val instance: AppManager = AppManager()
    }
}