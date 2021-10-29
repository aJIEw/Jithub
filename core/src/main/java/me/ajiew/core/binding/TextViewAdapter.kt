package me.ajiew.core.binding

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.request.ImageRequest
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.blankj.utilcode.util.StringUtils


@BindingAdapter("isBold")
fun setBold(textView: TextView, isBold: Boolean) {
    textView.setTypeface(null, if (isBold) Typeface.BOLD else Typeface.NORMAL)
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["ellipsizeLimit", "ellipsizeText"])
fun setEllipsize(textView: TextView, ellipsize: Int, ellipsizeText: String) {
    if (!TextUtils.isEmpty(ellipsizeText) && ellipsizeText.length > ellipsize) {
        textView.text = ellipsizeText.subSequence(0, ellipsize).toString() + "..."
    } else {
        textView.text = StringUtils.null2Length0(ellipsizeText)
    }
}

@BindingAdapter(
    value = [
        "drawableRightUrl", "drawableRightTint",
        "drawableRightWidth", "drawableRightHeight",
        "drawablePadding", "drawablePaddingTop"
    ],
    requireAll = false
)
fun drawableRightUrl(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    padding: Int, paddingTop: Int
) {
    loadImage(textView, url, tintColor, width, height, padding, paddingTop, 2)
}

@BindingAdapter(
    value = ["drawableLeftUrl", "drawableLeftTint",
        "drawableLeftWidth", "drawableLeftHeight",
        "drawablePadding", "drawablePaddingTop"],
    requireAll = false
)
fun drawableLeftUrl(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    padding: Int, paddingTop: Int
) {
    loadImage(textView, url, tintColor, width, height, padding, paddingTop, 0)
}

private fun loadImage(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    padding: Int, paddingTop: Int,
    direction: Int
) {
    if (!TextUtils.isEmpty(url) && width > 0 && height > 0) {
        val request = ImageRequest.Builder(textView.context)
            .data(url)
            .size(dp2px(width.toFloat()), dp2px(height.toFloat()))
            .target { drawable ->
                setIntrinsicBounds(drawable, paddingTop.coerceAtLeast(0))

                val wrapped = DrawableCompat.wrap(drawable)
                if (tintColor != 0) {
                    DrawableCompat.setTint(wrapped, tintColor)
                }
                textView.compoundDrawablePadding =
                    dp2px(padding.coerceAtLeast(0).toFloat())
                val drawables = textView.compoundDrawables
                when (direction) {
                    0 -> textView.setCompoundDrawables(
                        wrapped, drawables[1], drawables[2], drawables[3]
                    )
                    1 -> textView.setCompoundDrawables(
                        drawables[0], wrapped, drawables[2], drawables[3]
                    )
                    2 -> textView.setCompoundDrawables(
                        drawables[0], drawables[1], wrapped, drawables[3]
                    )
                    3 -> textView.setCompoundDrawables(
                        drawables[0], drawables[1], drawables[2], wrapped
                    )
                }
            }.build()

        textView.context.imageLoader.enqueue(request)
    }
}

private fun setIntrinsicBounds(drawable: Drawable?, paddingTop: Int) {
    drawable?.setBounds(
        0,
        dp2px(paddingTop.toFloat()),
        drawable.intrinsicWidth,
        drawable.intrinsicHeight + dp2px(paddingTop.toFloat())
    )
}

@BindingAdapter(value = ["htmlText"])
fun setHTMLText(textView: TextView, htmlText: String?) {
    if (htmlText == null) return
    textView.text = removeLineBreaks(
        HtmlCompat.fromHtml(
            htmlText,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    )
}

private fun removeLineBreaks(text: CharSequence): CharSequence {
    var copy = text
    while (copy.isNotEmpty() && copy[copy.length - 1] == '\n') {
        copy = copy.subSequence(0, copy.length - 1)
    }
    return copy
}
