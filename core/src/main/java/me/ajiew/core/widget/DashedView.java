package me.ajiew.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import me.ajiew.core.R;


public class DashedView extends View {

    public static int ORIENTATION_HORIZONTAL = 0;
    public static int ORIENTATION_VERTICAL = 1;

    private Paint mPaint;
    private int orientation;

    public DashedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int dashGap, dashLength, dashThickness;
        int color;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DashedView, 0, 0);

        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DashedView_dashGap, 5);
            dashLength = a.getDimensionPixelSize(R.styleable.DashedView_dashLength, 5);
            dashThickness = a.getDimensionPixelSize(R.styleable.DashedView_dashThickness, 3);
            color = a.getColor(R.styleable.DashedView_color, 0xff000000);
            orientation = a.getInt(R.styleable.DashedView_orientation, ORIENTATION_HORIZONTAL);
        } finally {
            a.recycle();
        }

        // 关闭硬件加速
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dashThickness);
        mPaint.setPathEffect(new DashPathEffect(new float[]{dashLength, dashGap,}, 0));
    }

    public DashedView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            float center = getHeight() * .5f;
            canvas.drawLine(0, center, getWidth(), center, mPaint);
        } else {
            float center = getWidth() * .5f;
            canvas.drawLine(center, 0, center, getHeight(), mPaint);
        }
    }
}