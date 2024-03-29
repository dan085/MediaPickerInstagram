package me.ningsk.cameralibrary.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <p>描述：自定义宽高比的FrameLayout<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 14<br>
 * 版本：v1.0<br>
 */
public class AspectFrameLayout extends FrameLayout {

    // 宽高比
    private double mTargetAspect = -1.0;

    public AspectFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AspectFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(double aspectRatio) {
        if (aspectRatio < 0) {
            throw  new IllegalArgumentException("ratio < 0");
        }
        if (mTargetAspect != aspectRatio) {
            mTargetAspect = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mTargetAspect > 0) {
            int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

            int horizPadding = getPaddingLeft() + getPaddingRight();
            int vertPadding = getPaddingTop() + getPaddingBottom();

            initialWidth -= horizPadding;
            initialHeight -= vertPadding;

            double viewAspectRatio = (double) initialWidth / initialHeight;
            double aspectDiff = mTargetAspect / viewAspectRatio - 1;

            if (Math.abs(aspectDiff) >= 0.01) {
                initialHeight = (int)(initialWidth / mTargetAspect);
            }
            initialWidth += horizPadding;
            initialHeight += vertPadding;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

