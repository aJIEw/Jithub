package me.ajiew.core.binding

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
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
 * */
@BindingAdapter(
    value = ["radiusUrl", "radius", "radiusBorderColor"],
    requireAll = false
)
fun setImageUrlWithRadius(
    imageView: ImageView, radiusUrl: String,
    radius: Int, @ColorInt radiusBorderColor: Int,
) {
    if (!TextUtils.isEmpty(radiusUrl) && !radiusUrl.contains("null")) {
        imageView.visibility = View.VISIBLE
        val context = imageView.context
        val request = ImageRequest.Builder(context)
            .data(radiusUrl).apply {
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