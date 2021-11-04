package me.ajiew.jithub.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.ajiew.core.base.BaseActivity
import me.ajiew.core.util.SPUtils
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.common.ViewModelFactory
import me.ajiew.jithub.data.response.AuthToken
import me.ajiew.jithub.databinding.ActivityMainBinding
import me.ajiew.jithub.ui.explore.ExploreFragment
import me.ajiew.jithub.ui.home.HomeFragment
import me.ajiew.jithub.ui.profile.ProfileFragment
import me.ajiew.jithub.ui.viewmodel.MainViewModel
import me.ajiew.jithub.widget.browser.CustomTabActivityHelper
import me.ajiew.jithub.widget.browser.WebviewFallback
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main
    override val viewModelId: Int = BR.vm
    override val viewModel: MainViewModel by viewModels {
        ViewModelFactory.instance
    }

    private lateinit var mainFragments: MutableList<Fragment>
    private lateinit var navView: BottomNavigationView

    private var currentIndex = -1

    override fun initData() {
        super.initData()

        currentIndex = if (hasLoggedIn) 0 else 1
    }

    override fun initView() {
        super.initView()

        setLightStatusBar()

        initFragment()

        setupBottomNavigationBar()
    }

    private fun setLightStatusBar() {
        val root = findViewById<View>(android.R.id.content).rootView

        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.background_dark)
        window.statusBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, root).isAppearanceLightStatusBars = true
    }

    private fun initFragment() {
        mainFragments = ArrayList<Fragment>()
        mainFragments.add(HomeFragment())
        mainFragments.add(ExploreFragment())
        mainFragments.add(ProfileFragment())
    }

    private fun commitAllowingStateLoss(position: Int) {
        hideAllFragment()
        val transaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag(position.toString())
        if (currentFragment != null) {
            transaction.show(currentFragment)
        } else {
            currentFragment = mainFragments[position]
            transaction.add(R.id.content_view, currentFragment, position.toString())
        }
        transaction.commitAllowingStateLoss()
    }

    private fun hideAllFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        for (i in mainFragments.indices) {
            val currentFragment = supportFragmentManager.findFragmentByTag(i.toString())
            if (currentFragment != null) {
                transaction.hide(currentFragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    private fun setupBottomNavigationBar() {
        navView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    currentIndex = 0

                    if (checkShowLoginPage()) {
                        return@setOnItemSelectedListener false
                    }
                    commitAllowingStateLoss(0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_explore -> {
                    currentIndex = 1

                    commitAllowingStateLoss(1)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_mine -> {
                    currentIndex = 2

                    if (checkShowLoginPage()) {
                        return@setOnItemSelectedListener false
                    }
                    commitAllowingStateLoss(2)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        // If not logged in, go to the explore tab
        if (currentIndex == 1) {
            navView.selectedItemId = R.id.nav_explore
        } else {
            commitAllowingStateLoss(0)
        }
    }

    private val hasLoggedIn get() = SPUtils.instance.getBoolean(Constants.SP_USER_LOGGED_IN)

    private fun checkShowLoginPage(): Boolean {
        val loggedIn = hasLoggedIn
        if (!loggedIn) {
            val url: String = Constants.GITHUB_OAUTH_AUTHORIZE_URL
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
                .build()
            CustomTabActivityHelper.openCustomTab(
                this, customTabsIntent, Uri.parse(url), WebviewFallback()
            )
        }
        return !loggedIn
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data.apply {
            if (this?.toString()?.startsWith(Constants.GITHUB_OAUTH_REDIRECT_URL) == true) {
                val code = getQueryParameter("code")
                if (!TextUtils.isEmpty(code)) {
                    viewModel.getAccessToken(code!!)
                }
            }
        }
    }

    override fun onSuccess(data: Any, message: String) {
        super.onSuccess(data, message)

        if (data is AuthToken) {
            setSelectedItem()
        }
    }

    private fun setSelectedItem() {
        navView.selectedItemId = when (currentIndex) {
            0 -> R.id.nav_home
            1 -> R.id.nav_explore
            2 -> R.id.nav_mine
            else -> throw IndexOutOfBoundsException("The index is out of the range!")
        }
    }

    override fun showLoading() {
        super.showLoading()

        binding.loadingView.visible = true
    }

    override fun hideLoading() {
        super.hideLoading()

        binding.loadingView.visible = false
    }
}