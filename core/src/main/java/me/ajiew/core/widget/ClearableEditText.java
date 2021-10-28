package me.ajiew.core.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import me.ajiew.core.R;


/**
 * To add a clear icon on EditText, use:
 *
 * <pre>
 * android:drawable(Right|Left)="@drawable/your_icon"
 * </pre>
 */
public class ClearableEditText extends AppCompatEditText
        implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable drawable;
    private Location drawableLocation = Location.RIGHT;
    private OnClearListener onClearListener;

    private OnTouchListener l;
    private OnFocusChangeListener f;


    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
        initIcon();
        setClearIconVisible(false);
    }

    private void initIcon() {
        drawable = null;
        if (drawableLocation != null) {
            drawable = getCompoundDrawables()[drawableLocation.idx];
        }
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.ic_clear);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        int min = getPaddingTop() + drawable.getIntrinsicHeight() + getPaddingBottom();
        if (getSuggestedMinimumHeight() < min) {
            setMinimumHeight(min);
        }
    }

    /**
     * set null to disables the icon
     */
    public void setIconLocation(Location loc) {
        this.drawableLocation = loc;
        initIcon();
    }

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initIcon();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getDisplayedDrawable() != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int left = (drawableLocation == Location.LEFT) ? 0 : getWidth() - getPaddingRight() - drawable.getIntrinsicWidth();
            int right = (drawableLocation == Location.LEFT) ? getPaddingLeft() + drawable.getIntrinsicWidth() : getWidth();
            boolean tappedX = x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (onClearListener != null) {
                        onClearListener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(!TextUtils.isEmpty(s));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private Drawable getDisplayedDrawable() {
        return (drawableLocation != null) ? getCompoundDrawables()[drawableLocation.idx] : null;
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable[] cd = getCompoundDrawables();
        Drawable displayed = getDisplayedDrawable();
        boolean wasVisible = (displayed != null);
        if (visible != wasVisible) {
            Drawable x = visible ? drawable : null;
            super.setCompoundDrawables((drawableLocation == Location.LEFT) ? x : cd[0],
                    cd[1],
                    (drawableLocation == Location.RIGHT) ? x : cd[2],
                    cd[3]);
        }
    }

    public interface OnClearListener {
        void didClearText();
    }

    public enum Location {
        LEFT(0), RIGHT(2);

        final int idx;

        Location(int idx) {
            this.idx = idx;
        }
    }
}
