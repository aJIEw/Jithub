package me.ajiew.core.binding

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
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

@BindingAdapter("android:drawableLeft")
fun setDrawableLeft(textView: TextView, resourceId: Int) {
    val drawable = ContextCompat.getDrawable(textView.context, resourceId)
    setIntrinsicBounds(drawable)
    setTextViewDrawables(textView, drawable, direction = 0)
}

@BindingAdapter("android:drawableTop")
fun setDrawableTop(textView: TextView, resourceId: Int) {
    val drawable = ContextCompat.getDrawable(textView.context, resourceId)
    setIntrinsicBounds(drawable)
    setTextViewDrawables(textView, drawable, direction = 1)
}

@BindingAdapter("android:drawableRight")
fun setDrawableRight(textView: TextView, resourceId: Int) {
    val drawable = ContextCompat.getDrawable(textView.context, resourceId)
    setIntrinsicBounds(drawable)
    setTextViewDrawables(textView, drawable, direction = 2)
}

@BindingAdapter("android:drawableBottom")
fun setDrawableBottom(textView: TextView, resourceId: Int) {
    val drawable = ContextCompat.getDrawable(textView.context, resourceId)
    setIntrinsicBounds(drawable)
    setTextViewDrawables(textView, drawable, direction = 3)
}

@BindingAdapter(
    value = [
        "drawableRightUrl", "drawableRightTint",
        "drawableRightWidth", "drawableRightHeight",
        "drawableRadius", "drawablePadding", "drawablePaddingTop"
    ],
    requireAll = false
)
fun drawableRightUrl(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    radius: Int, padding: Int, paddingTop: Int
) {
    loadImage(textView, url, tintColor, width, height, direction = 2, radius, padding, paddingTop)
}

@BindingAdapter(
    value = ["drawableLeftUrl", "drawableLeftTint",
        "drawableLeftWidth", "drawableLeftHeight",
        "drawableRadius", "drawablePadding", "drawablePaddingTop"
    ],
    requireAll = false
)
fun drawableLeftUrl(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    radius: Int, padding: Int, paddingTop: Int
) {
    loadImage(textView, url, tintColor, width, height, direction = 0, radius, padding, paddingTop)
}

private fun loadImage(
    textView: TextView,
    url: String,
    @ColorInt tintColor: Int,
    width: Int, height: Int,
    direction: Int,
    radius: Int = 0, padding: Int = 0, paddingTop: Int = 0,
) {
    if (!TextUtils.isEmpty(url) && width > 0 && height > 0) {
        val request = ImageRequest.Builder(textView.context)
            .data(url)
            .size(dp2px(width.toFloat()), dp2px(height.toFloat()))
            .transformations(RoundedCornersTransformation(dp2px(radius.toFloat()).toFloat()))
            .target { drawable ->
                setIntrinsicBounds(drawable, paddingTop.coerceAtLeast(0))

                textView.compoundDrawablePadding =
                    dp2px(padding.coerceAtLeast(0).toFloat())

                val wrapped = DrawableCompat.wrap(drawable)
                setTextViewDrawables(textView, wrapped, tintColor, direction)
            }.build()

        textView.context.imageLoader.enqueue(request)
    }
}

private fun setTextViewDrawables(
    textView: TextView, targetDrawable: Drawable?,
    @ColorInt tintColor: Int = 0, direction: Int
) {
    if (targetDrawable == null) return

    if (tintColor != 0) {
        DrawableCompat.setTint(targetDrawable, tintColor)
    }
    val drawables = textView.compoundDrawables
    when (direction) {
        // left
        0 -> textView.setCompoundDrawables(
            targetDrawable, drawables[1], drawables[2], drawables[3]
        )
        // top
        1 -> textView.setCompoundDrawables(
            drawables[0], targetDrawable, drawables[2], drawables[3]
        )
        // right
        2 -> textView.setCompoundDrawables(
            drawables[0], drawables[1], targetDrawable, drawables[3]
        )
        // bottom
        3 -> textView.setCompoundDrawables(
            drawables[0], drawables[1], drawables[2], targetDrawable
        )
    }
}

private fun setIntrinsicBounds(drawable: Drawable?, paddingTop: Int = 0) {
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

@BindingAdapter(value = ["drawableTintLeft"])
fun setDrawableLeftTint(textView: TextView, @ColorInt tintColor: Int) {
    val drawables = textView.compoundDrawables
    setTextViewDrawables(textView, drawables[0], tintColor, 0)
}

@BindingAdapter(value = ["drawableTintTop"])
fun setDrawableTopTint(textView: TextView, @ColorInt tintColor: Int) {
    val drawables = textView.compoundDrawables
    setTextViewDrawables(textView, drawables[1], tintColor, 1)
}

@BindingAdapter(value = ["drawableTintRight"])
fun setDrawableRightTint(textView: TextView, @ColorInt tintColor: Int) {
    val drawables = textView.compoundDrawables
    setTextViewDrawables(textView, drawables[2], tintColor, 2)
}

@BindingAdapter(value = ["drawableTintBottom"])
fun setDrawableBottomTint(textView: TextView, @ColorInt tintColor: Int) {
    val drawables = textView.compoundDrawables
    setTextViewDrawables(textView, drawables[3], tintColor, 3)
}

