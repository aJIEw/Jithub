package me.ajiew.jithub.ui

import android.content.Intent
import android.text.TextUtils
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.ajiew.core.base.BaseActivity
import me.ajiew.core.util.SPUtils
import me.ajiew.core.util.messenger.Messenger
import me.ajiew.core.util.setLightStatusAndNavBars
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.common.Constants
import me.ajiew.jithub.data.MessengerEvent.EVENT_TAB_DOUBLE_CLICK_EXPLORE
import me.ajiew.jithub.data.MessengerEvent.EVENT_TAB_DOUBLE_CLICK_HOME
import me.ajiew.jithub.data.response.AuthToken
import me.ajiew.jithub.databinding.ActivityMainBinding
import me.ajiew.jithub.ui.explore.ExploreFragment
import me.ajiew.jithub.ui.home.HomeFragment
import me.ajiew.jithub.ui.profile.ProfileFragment
import me.ajiew.jithub.ui.viewmodel.MainViewModel
import me.ajiew.jithub.util.AppUtil
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main
    override val viewModelId: Int = BR.vm
    override val viewModel: MainViewModel by viewModels()

    private lateinit var mainFragments: MutableList<Fragment>
    private lateinit var navView: BottomNavigationView

    private var currentIndex = -1

    override fun initData() {
        super.initData()

        currentIndex = if (hasLoggedIn) 0 else 1
    }

    override fun initView() {
        super.initView()

        setLightStatusAndNavBars(this)

        initFragment()

        setupBottomNavigationBar()
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

        navView.setOnItemReselectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Messenger.getDefault().sendNoMsg(EVENT_TAB_DOUBLE_CLICK_HOME)
                }
                R.id.nav_explore -> {
                    Messenger.getDefault().sendNoMsg(EVENT_TAB_DOUBLE_CLICK_EXPLORE)
                }
                R.id.nav_mine -> {

                }
            }
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
            AppUtil.openCustomTab(this, url, false)
        }
        return !loggedIn
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data.apply {
            if (this?.toString()?.startsWith(Constants.GITHUB_OAUTH_REDIRECT_URL) == true) {
                setLightStatusAndNavBars(this@MainActivity)

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