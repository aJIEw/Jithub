package me.ajiew.jithub.ui

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import me.ajiew.jithub.R
import me.ajiew.jithub.ui.explore.ExploreFragment
import me.ajiew.jithub.ui.home.HomeFragment
import me.ajiew.jithub.ui.profile.ProfileFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainFragments: MutableList<Fragment>
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLightStatusBar()

        initFragment()

        setupBottomNavigationBar()
    }

    private fun setLightStatusBar() {
        window.navigationBarColor = resources.getColor(android.R.color.background_dark)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun initFragment() {
        mainFragments = ArrayList<Fragment>()
        mainFragments.add(HomeFragment())
        mainFragments.add(ExploreFragment())
        mainFragments.add(ProfileFragment())

        commitAllowingStateLoss(0)
    }

    private fun commitAllowingStateLoss(position: Int) {
        hideAllFragment()
        val transaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag(position.toString() + "")
        if (currentFragment != null) {
            transaction.show(currentFragment)
        } else {
            currentFragment = mainFragments[position]
            transaction.add(R.id.content_view, currentFragment, position.toString() + "")
        }
        transaction.commitAllowingStateLoss()
    }

    private fun hideAllFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        for (i in mainFragments.indices) {
            val currentFragment = supportFragmentManager.findFragmentByTag(i.toString() + "")
            if (currentFragment != null) {
                transaction.hide(currentFragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    private fun setupBottomNavigationBar() {
        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    commitAllowingStateLoss(0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_mall -> {
                    commitAllowingStateLoss(1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_mine -> {
                    commitAllowingStateLoss(2)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}