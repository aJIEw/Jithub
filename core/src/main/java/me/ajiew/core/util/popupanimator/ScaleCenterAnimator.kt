package me.ajiew.core.util.popupanimator

import android.view.animation.OvershootInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.animator.PopupAnimator


class ScaleCenterAnimator : PopupAnimator {
    private var duration: Long

    constructor() {
        duration = XPopup.getAnimationDuration().toLong()
    }

    constructor(duration: Long) {
        this.duration = duration
    }

    override fun initAnimator() {
        targetView.scaleX = 0f
        targetView.scaleY = 0f
        targetView.alpha = 0f
        targetView.pivotX = (targetView.measuredWidth / 2).toFloat()
        targetView.pivotY = (targetView.measuredHeight / 2).toFloat()
    }

    override fun animateShow() {
        targetView.animate().scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(duration)
            .setInterpolator(OvershootInterpolator(1f))
            .withLayer()
            .start()
    }

    override fun animateDismiss() {
        targetView.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(duration)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .start()
    }
}