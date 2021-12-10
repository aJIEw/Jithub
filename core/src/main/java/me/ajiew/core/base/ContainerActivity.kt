package me.ajiew.core.base

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.R
import me.ajiew.core.base.viewmodel.BaseViewModel.Companion.LIGHT_STATUS_BAR
import me.ajiew.core.util.setLightStatusBar
import java.lang.ref.WeakReference

/**
 * 一个容器 Activity，用于盛装 Fragment，这样就不需要每个界面都在 AndroidManifest 中注册一遍
 */
@AndroidEntryPoint
open class ContainerActivity : AppCompatActivity() {

    private var mFragment: WeakReference<Fragment?> = WeakReference(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val fm: FragmentManager = supportFragmentManager
        var fragment: Fragment? = null
        if (savedInstanceState != null) {
            fragment = fm.getFragment(savedInstanceState, FRAGMENT_TAG)
        }
        if (fragment == null) {
            fragment = initFromIntent(intent)
        }

        val trans: FragmentTransaction = fm.beginTransaction()
        trans.replace(R.id.content, fragment)
        trans.commitAllowingStateLoss()
    }

    private fun initFromIntent(data: Intent?): Fragment {
        if (data == null) {
            throw RuntimeException(
                "you must provide a page info to display"
            )
        }
        try {
            val fragmentName = data.getStringExtra(FRAGMENT)
            require(!(fragmentName == null || "" == fragmentName)) { "can not find page fragmentName" }
            val fragmentClass = Class.forName(fragmentName)
            val fragment: Fragment = fragmentClass.newInstance() as Fragment
            val args = data.getBundleExtra(BUNDLE)
            if (args != null) {
                fragment.arguments = args

                if (args.getBoolean(LIGHT_STATUS_BAR)) {
                    setLightStatusBar(this)
                }
            }
            return fragment
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        throw RuntimeException("fragment initialization failed!")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mFragment.get() != null) {
            supportFragmentManager.putFragment(outState, FRAGMENT_TAG, mFragment.get()!!)
        }
    }

    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment is BaseFragment<*, *>) {
            if (!fragment.isBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "content_fragment_tag"
        const val FRAGMENT = "fragment"
        const val BUNDLE = "bundle"
    }
}