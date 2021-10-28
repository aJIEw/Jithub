package me.ajiew.core.binding

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ConvertUtils
import me.ajiew.core.ext.setOnClick
import me.ajiew.core.ext.setOnClickNoRepeat
import me.ajiew.core.util.messenger.BindingAction
import kotlin.math.roundToInt


@BindingAdapter(
    value = ["onClick", "dropQuickClick", "quickClickThreshold"],
    requireAll = false
)
fun onClick(
    view: View,
    onClick: BindingAction,
    dropQuickClick: Boolean = true,
    milliSeconds: Long = 500
) {
    if (dropQuickClick) {
        setOnClickNoRepeat(view, interval = milliSeconds) {
            onClick.call()
        }
    } else {
        setOnClick(view) {
            onClick.call()
        }
    }
}

@BindingAdapter("gone")
fun gone(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("dynamicWidth")
fun setLayoutWidth(view: View, width: Int) {
    val layoutParams = view.layoutParams
    layoutParams.width = ConvertUtils.dp2px(width.toFloat())
    view.layoutParams = layoutParams
}

@BindingAdapter("dynamicHeight")
fun setLayoutHeight(view: View, height: Int) {
    val layoutParams = view.layoutParams
    layoutParams.height = ConvertUtils.dp2px(height.toFloat())
    view.layoutParams = layoutParams
}

@BindingAdapter("app:generateId")
fun setGenerateId(view: View, generateId: Boolean) {
    if (generateId) {
        view.id = View.generateViewId()
    }
}

@BindingAdapter("android:layout_marginStart")
fun setLayoutMarginStart(view: View, marginStart: Float) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(marginStart.roundToInt(), p.topMargin, p.rightMargin, p.bottomMargin)
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginTop")
fun setLayoutMarginTop(view: View, marginTop: Float) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(p.leftMargin, marginTop.roundToInt(), p.rightMargin, p.bottomMargin)
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginEnd")
fun setLayoutMarginEnd(view: View, marginEnd: Float) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(p.leftMargin, p.topMargin, marginEnd.roundToInt(), p.bottomMargin)
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginBottom")
fun setLayoutMarginBottom(view: View, marginBottom: Float) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, marginBottom.roundToInt())
        view.requestLayout()
    }
}
