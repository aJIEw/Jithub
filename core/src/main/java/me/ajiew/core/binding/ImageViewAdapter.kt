package me.ajiew.core.binding

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.ConvertUtils.dp2px
import me.ajiew.core.widget.CircleCropStrokeTransformation


@BindingAdapter("imageViewTint")
fun setImageViewTint(imageView: ImageView, @ColorInt tintColor: Int) {
    if (tintColor != 0) {
        imageView.setColorFilter(tintColor)
    }
}

/**
 * Load image from url and set image radius
 *
 * @param radiusUrl image url
 * @param radius radius in dp
 * @param radiusBorderColor border color, it will override [radius] if both supplied
 * @param showPlaceholder weather to show placeholder while loading
 * @param placeholderRadius
 * @param placeholderStrokeWidth
 * @param errorDrawable drawable displayed while loading error or timeout
 * */
@BindingAdapter(
    value = ["radiusUrl", "radius", "radiusBorderColor",
        "radiusShowPlaceholder", "radiusPlaceholderWidth", "radiusPlaceholderStrokeWidth",
        "radiusErrorDrawable"],
    requireAll = false
)
fun setImageUrlWithRadius(
    imageView: ImageView, radiusUrl: String,
    radius: Int, @ColorInt radiusBorderColor: Int,
    showPlaceholder: Boolean, placeholderRadius: Int, placeholderStrokeWidth: Int,
    errorDrawable: Drawable?
) {
    if (!TextUtils.isEmpty(radiusUrl) && !radiusUrl.contains("null")) {
        imageView.visibility = View.VISIBLE
        val context = imageView.context
        val request = ImageRequest.Builder(context)
            .crossfade(true)
            .data(radiusUrl).apply {
                if (showPlaceholder) {
                    val drawable = CircularProgressDrawable(context)
                    drawable.centerRadius = dp2px((placeholderRadius.takeIf { it != 0 } ?: 12).toFloat()).toFloat()
                    drawable.strokeWidth = dp2px((placeholderStrokeWidth.takeIf { it != 0 } ?: 2).toFloat()).toFloat()
                    drawable.start()
                    placeholder(drawable)
                }

                if (errorDrawable != null) {
                    error(errorDrawable)
                }

                if (radius > 0) {
                    transformations(RoundedCornersTransformation(dp2px(radius.toFloat()).toFloat()))
                }

                if (radiusBorderColor != 0) {
                    transformations(
                        CircleCropStrokeTransformation(
                            CircleCropStrokeTransformation.Stroke(
                                4f,
                                radiusBorderColor
                            )
                        )
                    )
                }
            }.target(imageView).build()

        context.imageLoader.enqueue(request)
    } else {
        imageView.visibility = View.GONE
    }
}