package me.ajiew.core.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import java.util.*


@BindingAdapter("imageViewTint")
fun setImageViewTint(imageView: ImageView, @ColorInt tintColor: Int) {
    if (tintColor != 0) {
        imageView.setColorFilter(tintColor)
    }
}

/*
@BindingAdapter(
    value = ["radiusUrl", "radius", "radiusBorderColor", "layout_width", "layout_height"],
    requireAll = false
)
fun setImageUrlWithRadius(
    imageView: ImageView, url: String, radius: Int,
    @ColorInt radiusBorderColor: Int,
    imageWidth: Float, imageHeight: Float
) {
    if (!TextUtils.isEmpty(url) && !url.contains("null") && !url.endsWith("aliyuncs.com")) {
        imageView.visibility = View.VISIBLE
        val context = imageView.context
        val builder: RequestBuilder<Drawable> = Glide.with(context).load(url)
        if (radius > 0) {
            setupImageWithRadius(
                imageView,
                builder,
                radius,
                radiusBorderColor,
                imageWidth,
                imageHeight
            )
        } else {
            setupImageWithOriginal(imageView, builder, imageWidth, imageHeight)
        }
    } else {
        imageView.visibility = View.GONE
    }
}

private fun setupImageWithRadius(
    imageView: ImageView, builder: RequestBuilder<Drawable>,
    radius: Int, @ColorInt radiusBorderColor: Int,
    imageWidth: Float, imageHeight: Float
) {
    val transformationList: MutableList<Transformation<Bitmap>> =
        ArrayList<Transformation<Bitmap>>()
    transformationList.add(RoundedCorners(dp2px(radius)))
    if (radiusBorderColor != 0) {
        transformationList.add(GlideCircleBorderTransform(4f, radiusBorderColor))
    }
    val options: RequestOptions = RequestOptions().transforms(
        transformationList.toTypedArray()
    )
    if (imageWidth > 0 && imageHeight > 0) {
        val layoutParams = imageView.layoutParams
        layoutParams.width = dp2px(imageWidth)
        layoutParams.height = dp2px(imageHeight)
    }
    builder.apply(options).into(imageView)
}

private fun setupImageWithOriginal(
    imageView: ImageView, builder: RequestBuilder<Drawable>,
    imageWidth: Float, imageHeight: Float
) {
    val options: RequestOptions = RequestOptions()
        .fitCenter()
        .format(DecodeFormat.PREFER_ARGB_8888)
    if (imageWidth > 0 && imageHeight > 0) {
        val layoutParams = imageView.layoutParams
        layoutParams.width = dp2px(imageWidth)
        layoutParams.height = dp2px(imageHeight)
        builder.apply(options).into(imageView)
    } else {
        builder.apply(options.override(Target.SIZE_ORIGINAL)).into(imageView)
    }
}*/
