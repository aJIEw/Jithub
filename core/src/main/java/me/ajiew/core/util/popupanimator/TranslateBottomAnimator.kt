package me.ajiew.core.util.popupanimator

import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.animator.PopupAnimator


class TranslateBottomAnimator : PopupAnimator {

    private var duration: Long
    private var startTranslationX = 0f
    private var startTranslationY = 0f
    private var defTranslationX = 0f
    private var defTranslationY = 0f

    constructor() {
        duration = XPopup.getAnimationDuration().toLong()
    }

    constructor(duration: Long) {
        this.duration = duration
    }

    override fun initAnimator() {
        startTranslationX = targetView.translationX
        startTranslationY = targetView.translationY
        defTranslationX = targetView.translationX
        defTranslationY = targetView.translationY
        targetView.alpha = 0f
        targetView.translationY = targetView.measuredHeight /*+ halfHeightOffset*/.toFloat()
    }

    override fun animateShow() {
        targetView.animate()
            .translationX(defTranslationX).translationY(defTranslationY).alpha(1f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(duration)
            .withLayer()
            .start()
    }

    override fun animateDismiss() {
        targetView.animate()
            .translationX(startTranslationX).translationY(startTranslationY).alpha(0f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(duration)
            .withLayer()
            .start()
    }
}