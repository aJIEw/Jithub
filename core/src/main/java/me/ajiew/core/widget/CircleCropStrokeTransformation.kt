package me.ajiew.core.widget

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

/**
 * A [Transformation] that crops an image using a centered circle as the mask,
 * and add a stroke to it's border.
 * */
class CircleCropStrokeTransformation(
    private val strokeConfig: Stroke? = null
) : Transformation {

    override fun key(): String = javaClass.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val output = pool.get(minSize, minSize, input.config)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = XFERMODE
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)

            strokeConfig?.let { cnf ->
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    color = cnf.color
                    strokeWidth = cnf.widthPx
                }.let { drawCircle(radius, radius, radius - cnf.widthPx / 2f, it) }
            }
        }

        return output
    }

    override fun equals(other: Any?) = other is CircleCropStrokeTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "CircleCropStrokeTransformation()"

    data class Stroke(val widthPx: Float, val color: Int)

    private companion object {
        val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
}